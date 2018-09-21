package com.illuzor.lesson.wallpapers.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_list.*

abstract class AbstractFragment : Fragment() {

    protected lateinit var contentView: View
    protected abstract val layoutId: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(layoutId, container, false)

    protected fun showProgress() {
        contentView.visibility = View.GONE
        rotator.visibility = View.VISIBLE
        error_group.visibility = View.GONE
    }

    protected fun showContent() {
        contentView.visibility = View.VISIBLE
        rotator.visibility = View.GONE
        error_group.visibility = View.GONE
    }

    protected fun showError(@StringRes textId: Int, handler: () -> Unit) {
        contentView.visibility = View.GONE
        rotator.visibility = View.GONE
        error_group.visibility = View.VISIBLE
        tv_error.setText(textId)
        btn_retry.setOnClickListener { handler() }
    }

}