package com.illuzor.lesson.wallpapers.model

import com.illuzor.lesson.wallpapers.api.api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelCategories : ViewModelBase() {

    lateinit var data: List<Category>
        private set

    fun load() {
        state = State.PROGRESS
        api.categories().enqueue(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                data = response.body()!!
                state = State.LOADED
                loadListener()
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable?) {
                state = State.ERROR
                loadListener()
            }
        })
    }

}