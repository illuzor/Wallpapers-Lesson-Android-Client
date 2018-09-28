package com.illuzor.lesson.wallpapers.api

import com.illuzor.lesson.wallpapers.model.Category
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface WallpapersApi {

    @GET("categories.php")
    fun categories(): Call<List<Category>>

    @GET("gallery.php")
    fun gallery(@Query("gallery") galleryName: String): Call<List<String>>

    @Streaming
    @GET
    fun downloadFile(@Url fileUrl: String): Call<ResponseBody>

}
