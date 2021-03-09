package com.bp.digitalizacia_spravy_ciest.server

import android.content.Context
import com.bp.digitalizacia_spravy_ciest.utils.AuthInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceBuilder {

    var gson = GsonBuilder()
        .setLenient()
        .create()

    private val client = OkHttpClient.Builder().build()
    private val retrofit = Retrofit.Builder()

        .baseUrl("http://147.175.204.24")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    fun <T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
    //adding interceptor for token auth.. not used
    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
    }

}