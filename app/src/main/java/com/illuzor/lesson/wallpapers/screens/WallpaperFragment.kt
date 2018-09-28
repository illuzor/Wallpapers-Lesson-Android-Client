package com.illuzor.lesson.wallpapers.screens

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.illuzor.lesson.wallpapers.GlideApp
import com.illuzor.lesson.wallpapers.R
import com.illuzor.lesson.wallpapers.extensions.toast
import com.illuzor.lesson.wallpapers.model.ViewModelBase.State.*
import com.illuzor.lesson.wallpapers.model.ViewModelWallpaper
import kotlinx.android.synthetic.main.fragment_wallpaper.*
import java.io.File

@Suppress("PrivatePropertyName")
class WallpaperFragment : AbstractFragment() {

    override val layoutId = R.layout.fragment_wallpaper

    private var loaded = false
    private lateinit var relativeUrl: String
    private lateinit var model: ViewModelWallpaper
    private lateinit var imageFile: File

    private val CACHE_SIZE = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        model = ViewModelProviders.of(this).get(ViewModelWallpaper::class.java)

        val filename = arguments!!.getString("filename", "")
        val category = arguments!!.getString("category", "")
        relativeUrl = "images/$category/$filename"
        createFile("$category-$filename")
    }

    private fun createFile(filename: String) {
        val subfolder = File(context!!.cacheDir.path + "/wallpapers/")
        imageFile = subfolder.resolve(filename)
        if (imageFile.exists()) return
        if (subfolder.exists() && subfolder.listFiles().size >= CACHE_SIZE) {
            subfolder.listFiles().forEach { it.delete() }
        } else {
            subfolder.mkdir()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        contentView = pv_wallpaper

        if (model.state != PROGRESS && imageFile.exists()) {
            loadFileToImageView()
        } else {
            showProgress()
            model.setListener { checkData() }
            model.load(relativeUrl, imageFile)
        }
    }

    private fun checkData() {
        if (view == null) return

        when (model.state) {
            CREATED -> {
                showProgress()
                model.setListener { checkData() }
                model.load(relativeUrl, imageFile)
            }

            PROGRESS -> {
                showProgress()
                model.setListener { checkData() }
            }

            LOADED -> loadFileToImageView()

            ERROR -> {
                showError(R.string.unable_to_load) {
                    showProgress()
                    model.setListener { checkData() }
                    model.load(relativeUrl, imageFile)
                }
            }
        }
    }

    private fun loadFileToImageView() {
        showContent()
        GlideApp.with(context!!)
                .load(Uri.fromFile(imageFile))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(pv_wallpaper)
        loaded = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_wallpaper, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!loaded && item.itemId != android.R.id.home) {
            toast(R.string.not_loaded_yet)
        } else {
            when (item.itemId) {
                R.id.menu_item_set_wallpaper -> setWallpaper()
                R.id.menu_item_share_image -> shareWallpaper()
                R.id.menu_item_save_image -> checkPermissionForImageSave()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setWallpaper() {

    }

    private fun shareWallpaper() {

    }

    private fun checkPermissionForImageSave() {

    }

}