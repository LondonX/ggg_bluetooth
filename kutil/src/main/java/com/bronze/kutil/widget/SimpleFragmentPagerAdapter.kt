package com.bronze.kutil.widget

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by London on 2017/12/26.
 * simple adapter
 */
class SimpleFragmentPagerAdapter(fm: FragmentManager, vararg private val fragments: Fragment)
    : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size
}