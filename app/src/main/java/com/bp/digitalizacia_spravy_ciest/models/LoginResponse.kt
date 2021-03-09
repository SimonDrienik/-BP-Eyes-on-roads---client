package com.bp.digitalizacia_spravy_ciest.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status_code")
    var statusCode: Int,

    @SerializedName("auth_token")
    var authToken: String,

    @SerializedName("user")
    var user: User
)
