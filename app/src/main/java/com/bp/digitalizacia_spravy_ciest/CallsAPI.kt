package com.bp.digitalizacia_spravy_ciest

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.math.BigInteger
import com.bp.digitalizacia_spravy_ciest.ShowAllProblemsData as Problems1


interface CallsAPI {
    @GET("/showAllAndroid")
    fun getProblems() : Call<List<Problems1?>?>?

    @GET("/unregisteredPostAndroid/{poloha}/{popis_problemu}/{kategoria_problemu}/{stav_problemu}/{imgId}")
    suspend fun addProblem1(@Path("poloha") poloha: String?,
                            @Path("popis_problemu") popis_problemu: String?,
                            @Path("kategoria_problemu") kategoria_problemu: String?,
                            @Path("stav_problemu") stav_problemu: String?,
                            @Path("imgId") imgId: Int?): Response<ResponseBody>

    @Multipart
    @POST("/uploadProblemImage")
    fun postImage(@Part image: MultipartBody.Part, @Part("name") name: RequestBody): Call<BigInteger>
}

