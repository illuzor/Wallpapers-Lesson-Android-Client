package com.illuzor.lesson.wallpapers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.illuzor.lesson.wallpapers.R
import com.illuzor.lesson.wallpapers.api.BASE_URL
import com.illuzor.lesson.wallpapers.model.Category

class CategoriesAdapter : AbstractAdapter<Category, CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false))

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = getItem(position)

        holder.setTitle(item.name)
        holder.loadImage("$BASE_URL${item.preview}")
        holder.itemView.setOnClickListener { clickListener(item.name) }
    }

}