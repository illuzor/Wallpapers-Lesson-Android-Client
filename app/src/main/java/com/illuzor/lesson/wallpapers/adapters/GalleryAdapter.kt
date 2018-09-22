package com.illuzor.lesson.wallpapers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.illuzor.lesson.wallpapers.R
import com.illuzor.lesson.wallpapers.api.BASE_URL

class GalleryAdapter(private val category: String) : AbstractAdapter<String, GalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            GalleryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false))

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val item = getItem(position)
        holder.loadImage("${BASE_URL}images_previews/$category/$item")
        holder.itemView.setOnClickListener { clickListener(item) }
    }

}
