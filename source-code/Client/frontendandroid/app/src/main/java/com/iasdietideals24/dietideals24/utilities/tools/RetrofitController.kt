package com.iasdietideals24.dietideals24.utilities.tools

import com.iasdietideals24.dietideals24.utilities.services.Service
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitController {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor)
        .build()

    inline fun <reified T : Service> service(): T {
        return Retrofit.Builder()
            .baseUrl("http://localhost:8080")
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
            .create(T::class.java)
    }
}