package com.bronze.kutil.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.widget.TextView
import com.bronze.kutil.R

/**
 * Created by London on 2017/10/23.
 * progressDialog
 */
class LProgressDialog(context: Context,
                      private val dismissed: () -> Unit = {}) :
        Dialog(context) {
    init {
        setContentView(R.layout.dialog_progress)
        setCanceledOnTouchOutside(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOnDismissListener {
            dismissed.invoke()
        }
    }

    fun setMessage(@StringRes stringRes: Int) {
        setMessage(context.getString(stringRes))
    }

    fun setMessage(string: String) {
        val tv = findViewById<TextView>(R.id.dialog_progress_tvMessage)
        tv?.text = string
    }

    /*
     * dismiss dialog without calling #dismissed
     */
    fun dismissByUser() {
        setOnDismissListener(null)
        dismiss()
        setOnDismissListener {
            dismissed.invoke()
        }
    }
}
