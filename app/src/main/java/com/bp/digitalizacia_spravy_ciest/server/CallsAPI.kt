package com.bp.digitalizacia_spravy_ciest.server

import com.bp.digitalizacia_spravy_ciest.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.math.BigInteger
import com.bp.digitalizacia_spravy_ciest.models.ShowAllProblemsData as Problems1


interface CallsAPI {
    @GET("/showAllAndroid/{x}")
    fun getProblems(@Path("x") x: Int?) : Call<List<Problems1?>?>?

    @GET("/unregisteredPostAndroid/{poloha}/{popis_problemu}/{kategoria_problemu}/{stav_problemu}/{imgId}/{idOfUser}")
    fun addProblem1(@Path("poloha") poloha: String?,
                            @Path("popis_problemu") popis_problemu: String?,
                            @Path("kategoria_problemu") kategoria_problemu: String?,
                            @Path("stav_problemu") stav_problemu: String?,
                            @Path("imgId") imgId: Int?,
                            @Path("idOfUser") idOfUser: Int?): Call<Int>

    @Multipart
    @POST("/uploadProblemImage")
    fun postImage(@Part image: MultipartBody.Part, @Part("name") name: RequestBody): Call<BigInteger>

    @Multipart
    @POST("/api/uploadRiesenieImage")
    fun postRiesenieImg(@Part image: MultipartBody.Part, @Part("name") name: RequestBody, @Part("token") token: RequestBody, @Part("riesenieID") id: RequestBody) : Call<Int>

    @POST("/api/loginAndroid")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("/spinners")
    fun getSpinners() : Call<List<Spinners>>

    @GET("/downloadImg/{id}")
    fun getImg(@Path("id") id: Int?): Call<List<Imgs>>

    @GET("/history/{attribute}/{problemID}")
    fun getHistory(@Path("attribute") id: Int?, @Path("problemID") problemID: Int?): Call<List<ShowHistory>>

    @Headers("Content-Type: application/json")
    @POST("/api/delete")
    fun delete(@Body request: DeleteRequest): Call<Int>

    @POST("/api/editProblem")
    fun editProblem(@Body request: EditProblem): Call<Int>
}

