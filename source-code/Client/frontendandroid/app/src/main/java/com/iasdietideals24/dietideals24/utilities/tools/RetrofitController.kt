package com.iasdietideals24.dietideals24.utilities.tools

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.iasdietideals24.dietideals24.utilities.services.Service
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitController {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor)
        .build()

    val objectMapper: ObjectMapper = ObjectMapper().registerModule(JavaTimeModule())

    inline fun <reified T : Service> service(): T {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.163:55511")
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
            .create(T::class.java)
    }
}