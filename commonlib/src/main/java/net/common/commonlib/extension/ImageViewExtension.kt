package net.common.commonlib.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.File

object ImageViewExtension {

    fun ImageView.loadImage(url: String) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(this)
    }

    fun ImageView.loadImage(file: File) {
        Glide.with(context).load(file).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(this)
    }

}