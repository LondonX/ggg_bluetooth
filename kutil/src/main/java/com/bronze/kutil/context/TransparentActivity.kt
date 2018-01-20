package com.bronze.kutil.context

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager


/**
 * Created by London on 2017/6/16.
 * 透明状态栏
 */
open class TransparentActivity : AppCompatActivity() {
    var lightActionBar = false
        set(value) {
            field = value
            if (Build.VERSION.SDK_INT > 22) {
                if (field) {
                    window.decorView.systemUiVisibility =
                            window.decorView.systemUiVisibility or
                                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    window.decorView.systemUiVisibility =
                            window.decorView.systemUiVisibility
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowFlags()
    }

    private fun setWindowFlags() {
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            lightActionBar = lightActionBar()
        }
    }

    open fun lightActionBar(): Boolean = lightActionBar
}