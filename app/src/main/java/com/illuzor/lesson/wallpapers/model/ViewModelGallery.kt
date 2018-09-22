package com.illuzor.lesson.wallpapers.model

import com.illuzor.lesson.wallpapers.api.api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelGallery : ViewModelBase() {

    lateinit var data: List<String>
        private set

    fun load(category: String) {
        state = State.PROGRESS
        api.gallery(category).enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                data = response.body()!!
                state = State.LOADED
                loadListener()
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable?) {
                state = State.ERROR
                loadListener()
            }
        })
    }

}
