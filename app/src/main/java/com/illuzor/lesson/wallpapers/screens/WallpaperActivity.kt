package com.illuzor.lesson.wallpapers.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.illuzor.lesson.wallpapers.R

class WallpaperActivity : AbstractActivity() {

    override val layoutId = R.layout.activity_with_toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar(R.id.toolbar)
    }

    override fun getFragment(): Fragment {
        val args = Bundle()
        args.putString("filename", intent.getStringExtra("filename"))
        args.putString("category", intent.getStringExtra("category"))

        val fragment = WallpaperFragment()
        fragment.arguments = args
        return fragment
    }

}
