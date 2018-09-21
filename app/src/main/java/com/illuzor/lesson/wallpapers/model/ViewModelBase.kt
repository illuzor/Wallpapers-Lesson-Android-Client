package com.illuzor.lesson.wallpapers.model

import androidx.lifecycle.ViewModel

abstract class ViewModelBase : ViewModel() {

    enum class State { CREATED, PROGRESS, LOADED, ERROR }

    var state = State.CREATED
        protected set

    protected lateinit var loadListener: () -> Unit

    fun setListener(loadListener: () -> Unit) {
        this.loadListener = loadListener
    }

}