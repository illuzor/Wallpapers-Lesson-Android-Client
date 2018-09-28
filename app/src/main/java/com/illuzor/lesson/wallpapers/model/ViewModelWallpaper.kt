package com.illuzor.lesson.wallpapers.model

import com.illuzor.lesson.wallpapers.api.api
import com.illuzor.lesson.wallpapers.extensions.runInBackground
import com.illuzor.lesson.wallpapers.extensions.runInMainThread
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class ViewModelWallpaper : ViewModelBase() {

    private var file: File? = null

    fun load(url: String, file: File) {
        this.file = file
        state = State.PROGRESS
        api.downloadFile(url).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    runInBackground {
                        val inputStream = response.body()!!.byteStream()
                        inputStream.copyTo(FileOutputStream(file))
                        runInMainThread {
                            state = State.LOADED
                            loadListener()
                        }
                    }
                } else {
                    state = State.ERROR
                    loadListener()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                state = State.ERROR
                loadListener()
            }
        })
    }

    override fun onCleared() {
        if (state != State.LOADED && file != null && file!!.exists())
            file!!.delete()
    }

}