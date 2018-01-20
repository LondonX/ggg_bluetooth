package com.bronze.kutil

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

/**
 * Created by London on 2017/12/21.
 * quick broadcast receiver with register
 */
fun Context.registerReceiver(vararg actions: String, onReceived: (Intent) -> Unit): BroadcastReceiver {
    val filter = IntentFilter()
    actions.forEach { filter.addAction(it) }
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let(onReceived)
        }
    }
    registerReceiver(receiver, filter)
    return receiver
}

fun Context.safeUnregisterReceiver(receiver: BroadcastReceiver?) {
    try {
        receiver?.let { unregisterReceiver(receiver) }
    } catch (ignore: Exception) {
    }
}