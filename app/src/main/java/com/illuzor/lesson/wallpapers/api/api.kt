package com.illuzor.lesson.wallpapers.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://wallpapers.illuzor.com/"

val api: WallpapersApi =
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WallpapersApi::class.java)
