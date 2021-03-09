package com.bp.digitalizacia_spravy_ciest.utils

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

// adding token to requests..not used
class AuthInterceptor(context: Context) : Interceptor {
    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        //if token has been saved, add it to request
        sessionManager.fetchAuthToken()?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())

    }
}