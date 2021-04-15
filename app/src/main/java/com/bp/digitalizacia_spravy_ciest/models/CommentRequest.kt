package com.bp.digitalizacia_spravy_ciest.models

import com.google.gson.annotations.SerializedName

data class CommentRequest(
    @SerializedName("idProblem")
    var idProblem: Int,

    @SerializedName("komentText")
    var komentText: String,

    @SerializedName("token")
    var token: String,

    @SerializedName("userID")
    var userID: Int
)