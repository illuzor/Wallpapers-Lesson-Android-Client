package com.illuzor.lesson.wallpapers.screens

import android.Manifest
import android.app.WallpaperManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.illuzor.lesson.wallpapers.GlideApp
import com.illuzor.lesson.wallpapers.R
import com.illuzor.lesson.wallpapers.extensions.runInBackground
import com.illuzor.lesson.wallpapers.extensions.runInMainThread
import com.illuzor.lesson.wallpapers.extensions.toast
import com.illuzor.lesson.wallpapers.model.ViewModelBase.State.*
import com.illuzor.lesson.wallpapers.model.ViewModelWallpaper
import com.illuzor.lesson.wallpapers.ui.ProgressDialog
import kotlinx.android.synthetic.main.fragment_wallpaper.*
import java.io.File
import java.io.FileInputStream

@Suppress("PrivatePropertyName")
class WallpaperFragment : AbstractFragment() {

    override val layoutId = R.layout.fragment_wallpaper

    private val CACHE_SIZE = 20
    private val REQUEST_STORAGE_PERMISSION_CODE = 0
    private val SHARING_REQUEST_CODE = 1
    private val SETTING_REQUEST_CODE = 2
    private val PROVIDER_AUTHORITY = "com.illuzor.lesson.wallpapers.SHARE_WALLPAPER"
    private val imageFileUri by lazy { FileProvider.getUriForFile(activity!!, PROVIDER_AUTHORITY, imageFile) }

    private var loaded = false
    private lateinit var relativeUrl: String
    private lateinit var model: ViewModelWallpaper
    private lateinit var imageFile: File

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
        val setIntent = Intent(Intent.ACTION_SET_WALLPAPER)

        if (activity!!.packageManager.resolveActivity(setIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            val dialog = ProgressDialog()
            dialog.isCancelable = false
            dialog.title = getString(R.string.setting_up_wallpaper)
            dialog.show(fragmentManager, "")
            runInBackground {
                WallpaperManager.getInstance(activity).setStream(FileInputStream(imageFile))
                runInMainThread {
                    dialog.dismiss()
                    toast(R.string.wallpaper_set)
                }
            }
        } else {
            setIntent.action = Intent.ACTION_ATTACH_DATA
            setIntent.addCategory(Intent.CATEGORY_DEFAULT)
            setIntent.data = imageFileUri

            val activities = activity!!.packageManager.queryIntentActivities(setIntent, PackageManager.MATCH_DEFAULT_ONLY)
            for (activity in activities) {
                getActivity()!!.grantUriPermission(activity.activityInfo.packageName, imageFileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivityForResult(Intent.createChooser(setIntent, getString(R.string.set_as)), SETTING_REQUEST_CODE)
        }
    }

    private fun shareWallpaper() {
        val sharingIntent = ShareCompat.IntentBuilder
                .from(activity)
                .setType("image/*")
                .intent

        if (activity!!.packageManager.resolveActivity(sharingIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            toast(R.string.no_apps_for_share)
        } else {
            sharingIntent.putExtra(Intent.EXTRA_STREAM, imageFileUri)
            val activities = activity!!.packageManager.queryIntentActivities(sharingIntent, PackageManager.MATCH_DEFAULT_ONLY)
            for (activity in activities) {
                getActivity()!!.grantUriPermission(activity.activityInfo.packageName, imageFileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivityForResult(Intent.createChooser(sharingIntent, getString(R.string.share_image)), SHARING_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SHARING_REQUEST_CODE || requestCode == SETTING_REQUEST_CODE) {
            activity!!.revokeUriPermission(imageFileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    private fun checkPermissionForImageSave() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                activity!!.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            saveImageToGallery()
        } else {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_STORAGE_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            saveImageToGallery()
        } else {
            toast(R.string.unable_to_save_permission)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun saveImageToGallery() {
        val dialog = ProgressDialog()
        dialog.isCancelable = false
        dialog.title = getString(R.string.save_image)
        dialog.show(fragmentManager, "")
        runInBackground {
            MediaStore.Images.Media.insertImage(context!!.contentResolver, imageFile.path, "wallpaper", "")
            runInMainThread {
                dialog.dismiss()
                toast(R.string.saved)
            }
        }
    }

}