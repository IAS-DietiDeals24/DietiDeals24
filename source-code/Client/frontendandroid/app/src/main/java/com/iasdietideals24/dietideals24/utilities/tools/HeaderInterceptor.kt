package com.iasdietideals24.dietideals24.utilities.tools

import android.util.Log
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.core.Amplify
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer

object HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        runBlocking {
            Amplify.Auth.fetchAuthSession(
                {
                    val session = it as AWSCognitoAuthSession
                    if (session.identityIdResult.type == AuthSessionResult.Type.SUCCESS) {
                        CurrentUser.accessToken = session.accessToken ?: ""
                        Log.d("Amplify", "Fetch session successful: ${session.accessToken}")
                    }
                },
                {
                    Log.d(
                        "Amplify",
                        "Fetch session failed: ${it.message}; ${it.cause}; ${it.recoverySuggestion}"
                    )
                    CurrentUser.accessToken = ""
                }
            )
        }

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${CurrentUser.accessToken}")
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