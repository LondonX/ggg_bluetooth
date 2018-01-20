package com.bronze.kutil

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.bronze.kutil.widget.GlideApp
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

/**
 * Created by London on 2017/12/2.
 * web image loader based on glide
 */
fun ImageView.load(model: Any?,
                   @DrawableRes placeholder: Int = 0,
                   @DrawableRes error: Int = 0,
                   onSuccess: (Drawable) -> Unit = {}) {
    GlideApp.with(context.applicationContext)
            .load(model)
            .placeholder(placeholder)
            .error(error)
            .dontTransform()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?,
                                          model: Any?,
                                          target: Target<Drawable>?,
                                          isFirstResource: Boolean): Boolean = false

                override fun onResourceReady(resource: Drawable?,
                                             model: Any?,
                                             target: Target<Drawable>?,
                                             dataSource: DataSource?,
                                             isFirstResource: Boolean): Boolean {
                    resource?.let(onSuccess)
                    return false
                }

            })
            .into(this)
}

fun loadDrawable(context: Context,
                 model: Any?,
                 w: Int = -1,
                 h: Int = -1,
                 done: (Drawable?) -> Unit = {}) {
    val temp = GlideApp.with(context.applicationContext)
            .load(model)
            .dontTransform()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?,
                                          model: Any?,
                                          target: Target<Drawable>?,
                                          isFirstResource: Boolean): Boolean {
                    done.invoke(null)
                    return false
                }

                override fun onResourceReady(resource: Drawable?,
                                             model: Any?,
                                             target: Target<Drawable>?,
                                             dataSource: DataSource?,
                                             isFirstResource: Boolean): Boolean {
                    resource?.let(done)
                    return false
                }
            })
    if (w == -1 || h == -1) {
        temp.submit()
    } else {
        temp.submit(w, h)
    }
}