package com.bp.digitalizacia_spravy_ciest

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("welcome")
    fun getProblems(): Call<MutableList<Problems>>

}