package com.bronze.kutil.widget

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.bronze.kutil.inflate

/**
 * Created by London on 2017/7/6.
 * simple adapter for RecyclerView
 */
abstract class SimpleAdapter<T>(@LayoutRes val layoutId: Int, val dataList: ArrayList<T>) :
        RecyclerView.Adapter<SimpleHolder>() {

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        bindData(holder, position, dataList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder
            = SimpleHolder(parent.inflate(layoutId))

    override fun getItemCount(): Int = dataList.size

    abstract fun bindData(holder: SimpleHolder, position: Int, data: T)
}