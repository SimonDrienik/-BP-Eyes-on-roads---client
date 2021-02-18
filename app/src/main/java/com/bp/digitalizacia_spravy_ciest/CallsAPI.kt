package com.bp.digitalizacia_spravy_ciest

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import com.bp.digitalizacia_spravy_ciest.ShowAllProblemsData as Problems1


interface CallsAPI {
    @GET("/showAllAndroid")
    fun getProblems() : Call<List<Problems1?>?>?

    @GET("/unregisteredPostAndroid/{poloha}/{popis_problemu}/{kategoria_problemu}/{stav_problemu}")
    suspend fun addProblem1(@Path("poloha") poloha: String?,
                            @Path("popis_problemu") popis_problemu: String?,
                            @Path("kategoria_problemu") kategoria_problemu: String?,
                            @Path("stav_problemu") stav_problemu: String?): Response<ResponseBody>


}

