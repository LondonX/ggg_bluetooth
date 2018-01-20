package com.bronze.kutil.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.view.MotionEvent


/**
 * Created by London on 2017/12/25.
 * handler pointerIndex out of range error
 */
class HackyProblematicViewPager(context: Context) : ViewPager(context) {

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return try {
            super.onInterceptTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            //uncomment if you really want to see these errors
            //e.printStackTrace();
            false
        }
    }
}