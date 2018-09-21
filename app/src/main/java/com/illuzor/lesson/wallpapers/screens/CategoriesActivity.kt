package com.illuzor.lesson.wallpapers.screens

import com.illuzor.lesson.wallpapers.R

class CategoriesActivity : AbstractActivity() {

    override val layoutId = R.layout.activity_fragment

    override fun getFragment() = CategoriesFragment()
}
