package com.iasdietideals24.dietideals24.utilities.tools

import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.core.Amplify
import okhttp3.Interceptor
import okhttp3.Response

object HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Amplify.Auth.fetchAuthSession(
            {
                val session = it as AWSCognitoAuthSession
                if (session.identityIdResult.type == AuthSessionResult.Type.SUCCESS) {
                    CurrentUser.accessToken = session.accessToken ?: ""
                }
            },
            {
                CurrentUser.accessToken = ""
            }
        )

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${CurrentUser.accessToken}")
            .build()
        return chain.proceed(request)
    }
}