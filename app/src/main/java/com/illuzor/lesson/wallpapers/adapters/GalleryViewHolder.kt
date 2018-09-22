package com.illuzor.lesson.wallpapers.adapters

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.illuzor.lesson.wallpapers.GlideApp
import com.illuzor.lesson.wallpapers.R

class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val ivPreview: ImageView = view.findViewById(R.id.iv_preview)

    fun loadImage(url: String) =
            GlideApp.with(itemView)
                    .load(url)
                    .placeholder(R.drawable.placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivPreview)

}
