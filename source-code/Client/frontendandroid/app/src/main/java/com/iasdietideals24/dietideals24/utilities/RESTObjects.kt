package com.iasdietideals24.dietideals24.utilities

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitController {
    private const val BASE_URL = "http://192.168.1.217:55511"

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

object APIController {
    val instance: API by lazy {
        RetrofitController.instance.create(API::class.java)
    }
}