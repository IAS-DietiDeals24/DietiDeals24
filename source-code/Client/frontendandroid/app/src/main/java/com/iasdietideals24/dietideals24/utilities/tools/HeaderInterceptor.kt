package com.iasdietideals24.dietideals24.utilities.tools

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer

object HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .build()

        val buffer = Buffer()
        request.body?.writeTo(buffer)
        Log.d(
            "HeaderInterceptor",
            "Method: ${request.method}, Url: ${request.url}, Body: ${buffer.readUtf8()}"
        )

        val response = chain.proceed(request)
        Log.d("HeaderInterceptor", "${response.peekBody(Long.MAX_VALUE).string()}}")

        return response
    }
}