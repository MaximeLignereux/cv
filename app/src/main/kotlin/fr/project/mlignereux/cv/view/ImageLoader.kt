package fr.project.mlignereux.cv.view

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import javax.inject.Inject

interface ImageLoader {

    fun loadImage(url: String?, imageView : ImageView)

    fun loadCenterImage(url: String?, imageView : ImageView)

    class Implementation @Inject constructor(private val context: Context) : ImageLoader {

        override fun loadImage(url: String?, imageView: ImageView) {
            Glide.with(context).load(url).skipMemoryCache(true).into(imageView)
        }

        override fun loadCenterImage(url: String?, imageView: ImageView) {
            Glide.with(context).load(url).skipMemoryCache(true).centerCrop().into(imageView)
        }

    }
}