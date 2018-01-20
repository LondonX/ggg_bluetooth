package com.bronze.kutil.adapter

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bronze.kutil.load
import com.github.chrisbanes.photoview.PhotoView

/**
 * Created by London on 2017/11/10.
 * 图片放大
 */
class PhotoAdapter(private val models: List<Any>, private val photoClick: (PhotoView) -> Unit) : PagerAdapter() {
    private val convertViews = HashSet<View>()

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`
    override fun getCount() = models.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val v = convertViews.find { true } ?: PhotoView(container.context)
        convertViews.remove(v)
        container.addView(v)
        v.layoutParams?.let {
            it.width = ViewPager.LayoutParams.MATCH_PARENT
            it.height = ViewPager.LayoutParams.MATCH_PARENT
            v.layoutParams = it
        }
        v.setOnClickListener { photoClick.invoke(it as PhotoView) }
        (v as ImageView).load(models[position])
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val v = `object` as View
        convertViews.add(v)
        container.removeView(v)
    }
}