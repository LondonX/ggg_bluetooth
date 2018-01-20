package com.bronze.kutil

import android.app.Activity
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import org.jetbrains.anko.contentView

/**
 * Created by London on 2017/9/30.
 * tools for snackBar
 */
fun Activity.showSnack(@StringRes textRes: Int,
                       duration: Int = Snackbar.LENGTH_SHORT,
                       @StringRes actionTextRes: Int = 0,
                       action: ((Snackbar) -> Unit)? = null) {
    showSnack(getString(textRes), duration, actionTextRes, action)
}

fun Activity.showSnack(text: CharSequence,
                       duration: Int = Snackbar.LENGTH_SHORT,
                       @StringRes actionTextRes: Int = 0,
                       action: ((Snackbar) -> Unit)? = null) {
    this.contentView?.let {
        val sb = Snackbar.make(it, text, duration)
        action?.let actionSetting@ {
            if (actionTextRes == 0) {
                return@actionSetting
            }
            sb.setAction(actionTextRes) {
                action.invoke(sb)
            }
        }
        sb.show()
    }
}