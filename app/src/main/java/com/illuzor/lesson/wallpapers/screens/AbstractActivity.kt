package com.illuzor.lesson.wallpapers.screens

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.illuzor.lesson.wallpapers.R

abstract class AbstractActivity : AppCompatActivity() {

    protected abstract val layoutId: Int

    protected abstract fun getFragment(): Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        var fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment == null) {
            fragment = getFragment()
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, fragment)
                    .commit()
        }
    }

    protected fun setToolbar(@IdRes id: Int, title: String = "", backButton: Boolean = true) {
        val toolbar: Toolbar = findViewById(id)
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(backButton)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

}
