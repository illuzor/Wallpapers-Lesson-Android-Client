package com.illuzor.lesson.wallpapers.adapters

import androidx.recyclerview.widget.RecyclerView

abstract class AbstractAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private val items = mutableListOf<T>()

    protected lateinit var clickListener: (String) -> Unit

    fun setOnclickListener(clickListener: (String) -> Unit) {
        this.clickListener = clickListener
    }

    fun addItems(items: List<T>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    protected fun getItem(position: Int): T = items[position]
}