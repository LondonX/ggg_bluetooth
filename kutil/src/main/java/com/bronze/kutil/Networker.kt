package com.bronze.kutil

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * Created by London on 2017/7/25.
 * networker
 */
private val client: OkHttpClient by lazy {
    OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS).build()
}
private val uiHandler = Handler(Looper.getMainLooper())
private var globalResponder: (statusCode: Int, body: String?) -> Boolean = { _, _ -> false }

fun networkerGlobalResponder(responder: (statusCode: Int, body: String?) -> Boolean) {
    globalResponder = responder
}

private fun doRequestAsync(call: Call, f: (statusCode: Int, body: String?) -> Unit, charset: Charset) {
    Thread {
        val resp = doRequest(call, charset)
        uiHandler.post {
            call.cancel()
            if (globalResponder.invoke(resp?.statusCode ?: 0, resp?.body)) {
                return@post
            }
            f.invoke(resp?.statusCode ?: 0, resp?.body)
        }
    }.start()
    return
}

private fun doRequest(call: Call, charset: Charset): MyResponse? {
    try {
        if (call.isCanceled) {
            return null
        }
        val response = call.execute()
        val status = response.code()
        val bs = response.body()?.bytes() ?: return null
        val body = String(bs, charset)
        response?.close()
        call.cancel()
        if (status !in (200..300)) {
            Log.e("Networker", call.request().url().toString() + " statusCode:" + status)
        }
        return MyResponse(status, body)
    } catch (ignore: SocketException) {
    } catch (ignore: SocketTimeoutException) {
    } catch (ignore: ConnectException) {
    } catch (ignore: IOException) {
    } finally {
        call.cancel()
    }
    return null
}

fun String.urlParam(param: Param? = null): String {
    var temp = this
    param?.map?.forEach { it ->
        temp = temp.replace("{${it.key}}", it.value.toString())
    }
    return temp
}

fun String.httpGet(param: Param? = null,
                   charset: Charset = Charset.defaultCharset(),
                   f: (statusCode: Int, body: String?) -> Unit): Call {
    val call = client.newCall(Request.Builder()
            .url(this + (param?.toGetParam() ?: ""))
            .build())
    doRequestAsync(call, f, charset)
    return call
}

fun String.httpPost(param: Param? = null,
                    json: Boolean = false,
                    charset: Charset = Charset.defaultCharset(),
                    f: (statusCode: Int, body: String?) -> Unit): Call {
    val body = if (json) param?.toJSONBody() else param?.toFormBody()
    val builder = Request.Builder()
    if (body != null) {
        builder.post(body)
    }
    val call = client.newCall(builder
            .url(this)
            .build())
    doRequestAsync(call, f, charset)
    return call
}

fun String.httpPut(param: Param? = null,
                   json: Boolean = false,
                   charset: Charset = Charset.defaultCharset(),
                   f: (statusCode: Int, body: String?) -> Unit): Call {
    val body = if (json) param?.toJSONBody() else param?.toFormBody()
    val builder = Request.Builder()
    if (body != null) {
        builder.put(body)
    }
    val call = client.newCall(builder
            .url(this)
            .build())
    doRequestAsync(call, f, charset)
    return call
}

fun String.httpDelete(param: Param? = null,
                      json: Boolean = false,
                      charset: Charset = Charset.defaultCharset(),
                      f: (statusCode: Int, body: String?) -> Unit): Call {
    val body = if (json) param?.toJSONBody() else param?.toFormBody()
    val builder = Request.Builder()
    if (body != null) {
        builder.delete(body)
    }
    val call = client.newCall(builder
            .url(this)
            .build())
    doRequestAsync(call, f, charset)
    return call
}

fun String.httpRawPut(body: RequestBody?,
                      charset: Charset = Charset.defaultCharset()): MyResponse? {
    val builder = Request.Builder()
    if (body != null) {
        builder.put(body)
    }
    val call = client.newCall(builder
            .url(this)
            .build())
    return doRequest(call, charset)
}

open class Param(k: String? = null, v: Any? = null) {
    val map = LinkedHashMap<String, Any?>()

    init {
        if (!k.isNullOrEmpty()) {
            map.put(k!!, v)
        }
    }

    fun put(k: String, v: Any?): Param {
        map.put(k, v)
        return this
    }

    fun remove(k: String): Param {
        map.remove(k)
        return this
    }

    fun toGetParam(): String {
        val sb = StringBuilder()
        map.forEach { it ->
            if (sb.isEmpty()) {
                sb.append("?")
            } else {
                sb.append("&")
            }
            sb.append("${it.key}=${URLEncoder.encode(it.value.toString(), "utf-8")}")
        }
        return sb.toString()
    }

    fun toFormBody(): RequestBody {
        val builder = FormBody.Builder()
        map.forEach { it ->
            builder.add(it.key, it.value.toString())
        }
        return builder.build()
    }

    fun toJSONBody(): RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                    Gson().toJson(map))
}

data class MyResponse(val statusCode: Int, val body: String?)
