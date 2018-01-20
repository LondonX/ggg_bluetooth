package com.bronze.kutil

import android.content.Context
import android.content.res.Resources
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


/**
 * Created by London on 2017/5/26.
 * 拓展类
 */
val gson: Gson by lazy { Gson() }

fun Any.log(string: String) {
    Log.i(javaClass.simpleName, string)
}

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun ViewGroup.forEach(f: (childView: View) -> Unit) {
    (0 until childCount).forEach { index -> f.invoke(this.getChildAt(index)) }
}

fun ViewGroup.forEachIndex(f: (index: Int, childView: View) -> Unit) {
    (0 until childCount).forEach { index -> f.invoke(index, this.getChildAt(index)) }
}

fun ImageView.setImageMipmap(mipmapName: String) {
    val resID = resources.getIdentifier(mipmapName, "mipmap", context.packageName)
    setImageResource(resID)
}

fun View.onLayout(f: (View) -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener {
        f.invoke(this)
    }
}

fun EditText.setPasswordVisible(visible: Boolean) {
    val currentSelection = selectionStart
    this.transformationMethod = if (visible) {
        HideReturnsTransformationMethod.getInstance()
    } else {
        PasswordTransformationMethod.getInstance()
    }
    this.setSelection(currentSelection)
}

fun Context.removePref(key: String) {
    val editor = getSharedPreferences(packageName + "_pref.xml", Context.MODE_PRIVATE).edit()
    editor.remove(key)
    editor.apply()
}

fun Context.savePref(key: String, value: Any) {
    val editor = getSharedPreferences(packageName + "_pref.xml", Context.MODE_PRIVATE).edit()
    //B F I L S
    when (value) {
        is Boolean -> editor.putBoolean(key, value)
        is Float -> editor.putFloat(key, value)
        is Int -> editor.putInt(key, value)
        is Long -> editor.putLong(key, value)
        is String -> editor.putString(key, value)
        else -> editor.putString(key, gson.toJson(value))
    }
    editor.apply()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T> Context.loadPref(key: String, defaultValue: T? = null): T {
    val sp = getSharedPreferences(packageName + "_pref.xml", Context.MODE_PRIVATE)
    return when (defaultValue) {
        is Boolean -> sp.getBoolean(key, defaultValue) as T
        is Float -> sp.getFloat(key, defaultValue) as T
        is Int -> sp.getInt(key, defaultValue) as T
        is Long -> sp.getLong(key, defaultValue) as T
        is String -> sp.getString(key, defaultValue) as T
        is Collection<*> -> gson.fromJson(sp.getString(key, "[]"), object : TypeToken<T>() {}.type)
        else -> gson.fromJson(sp.getString(key, "{}"), object : TypeToken<T>() {}.type)
    }
}

fun Context.hideKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.showKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, 0)
}

fun String?.isJSON(): Boolean {
    if (this == null) {
        return false
    }
    return try {
        JSONObject(this)
        true
    } catch (ignore: JSONException) {
        try {
            JSONArray(this)
            true
        } catch (ignore: JSONException) {
            false
        }
    }
}

fun String?.isIp4(): Boolean {
    if (this == null) {
        return false
    }
    return Patterns.IP_ADDRESS.matcher(this).matches()
}

fun String?.isURL(): Boolean {
    if (this == null) {
        return false
    }
    if (!this.startsWith("http") && !this.startsWith("HTTP")) {
        return false
    }
    return Patterns.WEB_URL.matcher(this).matches()
}

fun String?.isPhoneNumber(): Boolean {
    if (this == null) {
        return false
    }
    return Patterns.PHONE.matcher(this).matches()
}

val Int.dp: Float
    get() {
        return this.toFloat().dp
    }

val Float.dp: Float
    get() {
        return this * Resources.getSystem().displayMetrics.density
    }

fun Byte.toUInt(): Int = this.toInt() and 0xFF

fun Short.toUInt(): Int = this.toInt() and 0xFFFF

fun String.extractInt(def: Int = 0): Int {
    if (this.isEmpty()) {
        return def
    }
    var str2 = ""
    this.forEach { c ->
        if (c >= 48.toChar() && c <= 57.toChar()) {
            str2 += c
        }
    }
    if (str2.isEmpty()) {
        return def
    }
    val temp = str2.toIntOrNull() ?: return def
    return temp
}

inline fun <reified T> JSONObject.getOrNull(k: String): T? {
    return when (T::class) {
        String::class -> if (this.has(k)) this.getString(k) as? T else null
        Int::class -> if (this.has(k)) this.getInt(k) as? T else null
        JSONObject::class -> if (this.has(k)) this.getJSONObject(k) as? T else null
        Boolean::class -> if (this.has(k)) this.getBoolean(k) as? T else null
        Double::class -> if (this.has(k)) this.getDouble(k) as? T else null
        Float::class -> if (this.has(k)) this.getDouble(k).toFloat() as? T else null
        Long::class -> if (this.has(k)) this.getLong(k) as? T else null
        JSONArray::class -> if (this.has(k)) this.getJSONArray(k) as? T else null
        else -> null
    }
}