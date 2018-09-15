package com.illuzor.lesson.wallpapers.api

import com.illuzor.lesson.wallpapers.model.Category
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WallpapersApi {

    @GET("categories.php")
    fun categories(): Call<List<Category>>

    @GET("gallery.php")
    fun gallery(@Query("gallery") galleryName: String): Call<List<String>>

}
