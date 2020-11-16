package com.bp.digitalizacia_spravy_ciest

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import com.bp.digitalizacia_spravy_ciest.Problems as Problems1


interface problemsAPI {
    @GET("/showAllAndroid")
    fun getProblems() : Call<List<Problems1?>?>?

    @POST("/unregisteredPostAndroid")
    Call<ApiResponse> request()
}

