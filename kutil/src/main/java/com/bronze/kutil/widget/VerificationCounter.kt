package com.bronze.kutil.widget

import android.graphics.Color
import android.widget.TextView
import com.bronze.kutil.log
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.textColor
import org.jetbrains.anko.uiThread
import java.util.concurrent.Future

/**
 * Created by London on 2017/10/24.
 * 验证码倒计时
 */
class VerificationCounter(private val btnFetch: TextView,
                          private val colorDisabled: Int = Color.LTGRAY,
                          private val intervalSec: Int = 30,
                          private val fmt: String = "%s(%d)") {
    private val defaultColor = btnFetch.currentTextColor
    private val defaultText = btnFetch.text

    private var counting: Future<Unit>? = null
    private var sec = intervalSec

    fun start() {
        counting?.cancel(true)
        btnFetch.isEnabled = false
        btnFetch.textColor = colorDisabled
        sec = intervalSec
        counting = doAsync {
            while (sec > 0) {
                if (!btnFetch.isAttachedToWindow) {
                    return@doAsync
                }
                uiThread {
                    log("Counting:$sec")
                    btnFetch.text = String.format(fmt, defaultText, sec)
                    sec--
                }
                Thread.sleep(1000)
            }
            uiThread {
                btnFetch.isEnabled = true
                btnFetch.textColor = defaultColor
                btnFetch.text = defaultText
            }
        }
    }

    fun stop() {
        sec = 0
    }
}