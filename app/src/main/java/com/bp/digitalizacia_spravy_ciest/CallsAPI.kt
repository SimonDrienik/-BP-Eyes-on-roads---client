package com.bp.digitalizacia_spravy_ciest

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.bp.digitalizacia_spravy_ciest.ShowAllProblemsData as Problems1


interface CallsAPI {
    @GET("/showAllAndroid")
    fun getProblems() : Call<List<Problems1?>?>?

    @POST("/unregisteredPostAndroid")
    fun addProblem(@Body problemDataData: UnregisteredPostProblemData): Call<UnregisteredPostProblemData>
}

