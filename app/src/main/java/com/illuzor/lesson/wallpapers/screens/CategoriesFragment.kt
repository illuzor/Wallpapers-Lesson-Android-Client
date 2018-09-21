
package com.illuzor.lesson.wallpapers.screens

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.illuzor.lesson.wallpapers.R
import com.illuzor.lesson.wallpapers.adapters.CategoriesAdapter
import com.illuzor.lesson.wallpapers.model.ViewModelCategories
import com.illuzor.lesson.wallpapers.model.ViewModelBase.State.*
import kotlinx.android.synthetic.main.fragment_list.*

class CategoriesFragment : AbstractFragment() {

    override val layoutId = R.layout.fragment_list
    private val adapter = CategoriesAdapter()
    private lateinit var model: ViewModelCategories

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(ViewModelCategories::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        contentView = recycler_view
        recycler_view.layoutManager = GridLayoutManager(context, 2)
        recycler_view.adapter = adapter

        checkData()
    }

    private fun checkData() {
        if (view == null) return

        when (model.state) {
            CREATED -> {
                showProgress()
                model.setListener { checkData() }
                model.load()
            }

            PROGRESS -> {
                showProgress()
                model.setListener { checkData() }
            }

            ERROR -> {
                showError(R.string.unable_to_load) {
                    showProgress()
                    model.setListener { checkData() }
                    model.load()
                }
            }

            LOADED -> {
                adapter.addItems(model.data.sortedBy { it.name })
                showContent()

                adapter.setOnclickListener { category ->

                }

            }
        }
    }

}