package com.bronze.kutil.context

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.bronze.kutil.adapter.PhotoAdapter
import com.bronze.kutil.view.HackyProblematicViewPager
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.startActivity

class PhotoActivity : TransparentActivity() {
    companion object {
        private var models = emptyList<Any>()
        private val point = arrayOf(0f, 0f)
        private var index = 0
        fun start(context: Context, index: Int, fromView: View?, models: List<Any>) {
            Companion.models = models
            Companion.index = index
            if (fromView == null) {
                point[0] = 0f
                point[1] = 0f
            } else {
                point[0] = fromView.x
                point[1] = fromView.y
            }
            context.startActivity<PhotoActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pagerPhoto = HackyProblematicViewPager(this)
        setContentView(pagerPhoto)
        pagerPhoto.backgroundColor = Color.BLACK
        pagerPhoto.adapter = PhotoAdapter(models) { finish() }
        pagerPhoto.currentItem = index
        if (point.sum() != 0f && point[0] * point[1] != 0f) {

        }
    }
}
