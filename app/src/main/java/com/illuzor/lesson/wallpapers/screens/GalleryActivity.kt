package com.illuzor.lesson.wallpapers.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.illuzor.lesson.wallpapers.R

class GalleryActivity : AbstractActivity() {

    override val layoutId = R.layout.activity_with_toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar(R.id.toolbar, intent.getStringExtra("category"))
    }

    override fun getFragment(): Fragment {
        val args = Bundle()
        args.putString("category", intent.getStringExtra("category"))

        val fragment = GalleryFragment()
        fragment.arguments = args
        return fragment
    }

}
