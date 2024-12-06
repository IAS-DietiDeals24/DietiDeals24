package com.iasdietideals24.dietideals24.utilities.tools

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer

object HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = if (CurrentUser.jwt == "") {
            chain.request().newBuilder()
                .build()
        } else {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${CurrentUser.jwt}")
                .build()
        }

        val buffer = Buffer()
        request.body?.writeTo(buffer)
        Log.d(
            "HeaderInterceptor",
            "Method: ${request.method}, Url: ${request.url}, Body: ${buffer.readUtf8()}"
        )

        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_2)
                .code(408)
                .message("Request Timeout")
                .addHeader("Content-Type", "application/json")
                .body("".toResponseBody())
                .build()
        }
        Log.d("HeaderInterceptor", response.peekBody(Long.MAX_VALUE).string())

        return response
    }
}