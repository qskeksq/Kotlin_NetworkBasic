package network.kotlin.flow9.net.networkbasic.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget

import java.io.File

class ImageUtil {

    companion object {

        fun SimpleImageLoad(context: Context, drawable: Int, view: ImageView) {
            Glide.with(context)
                    .load(drawable)
                    .error(0)
                    .placeholder(0)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view)
        }

        fun SimpleImageLoad(context: Context, file: File, view: ImageView) {
            val imageUri = Uri.fromFile(file)
            Glide.with(context)
                    .load(imageUri)
                    .error(0)
                    .placeholder(0)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(view)
        }

        fun SimpleImageLoad(context: Context, url: String, view: ImageView) {
            Glide.with(context)
                    .load(url)
                    .error(0)
                    .placeholder(0)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(view)
        }

        fun ImageLoadWithBitmapTarget(context: Context, url: String, view: ImageView) {
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .error(0)
                    .placeholder(0)
                    .into(object : BitmapImageViewTarget(view) {
                        protected override fun setResource(resource: Bitmap) {
                            super.setResource(resource)
                        }
                    })
        }

        fun ImageLoadWithSimpleTarget(context: Context, url: String, view: ImageView) {
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .error(0)
                    .placeholder(0)
                    .into(object : SimpleTarget<Bitmap>(250, 250) {
                        override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {

                        }
                    })
        }
    }
}
