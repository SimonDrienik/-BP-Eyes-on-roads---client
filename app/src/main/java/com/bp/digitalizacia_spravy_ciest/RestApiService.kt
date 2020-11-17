package com.bp.digitalizacia_spravy_ciest

import com.bp.digitalizacia_spravy_ciest.ServiceBuilder.buildService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiService {
    fun addProblem(problemDataData: UnregisteredPostProblemData, onResult: (UnregisteredPostProblemData?) -> Unit){
        val retrofit = buildService(CallsAPI::class.java)
        retrofit.addProblem(problemDataData).enqueue(
            object : Callback<UnregisteredPostProblemData> {
                override fun onFailure(call: Call<UnregisteredPostProblemData>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<UnregisteredPostProblemData>, response: Response<UnregisteredPostProblemData>) {
                    val addedProblem = response.body()
                    onResult(addedProblem)
                }
            }
        )
    }
}