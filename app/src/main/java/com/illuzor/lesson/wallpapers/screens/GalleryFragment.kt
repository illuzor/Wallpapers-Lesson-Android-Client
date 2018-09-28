package com.illuzor.lesson.wallpapers.screens

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.illuzor.lesson.wallpapers.R
import com.illuzor.lesson.wallpapers.adapters.GalleryAdapter
import com.illuzor.lesson.wallpapers.model.ViewModelGallery
import com.illuzor.lesson.wallpapers.model.ViewModelBase.State.*
import kotlinx.android.synthetic.main.fragment_list.*

class GalleryFragment : AbstractFragment() {

    override val layoutId = R.layout.fragment_list
    private lateinit var category: String
    private lateinit var adapter: GalleryAdapter
    private lateinit var model: ViewModelGallery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        category = arguments!!.getString("category", "")
        adapter = GalleryAdapter(category)
        model = ViewModelProviders.of(this).get(ViewModelGallery::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        contentView = recycler_view
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = adapter
        checkData()
    }

    private fun checkData() {
        if (view == null) return

        when (model.state) {
            CREATED -> {
                showProgress()
                model.setListener { checkData() }
                model.load(category)
            }

            PROGRESS -> {
                showProgress()
                model.setListener { checkData() }
            }

            LOADED -> {
                showContent()
                adapter.addItems(model.data)
                adapter.setOnclickListener { filename ->
                    val intent = Intent(context, WallpaperActivity::class.java)
                    intent.putExtra("filename", filename)
                    intent.putExtra("category", category)
                    startActivity(intent)
                }
            }

            ERROR -> {
                showError(R.string.unable_to_load) {
                    showProgress()
                    model.setListener { checkData() }
                    model.load(category)
                }
            }
        }
    }

}
