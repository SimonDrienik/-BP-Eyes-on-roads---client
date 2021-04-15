package com.bp.digitalizacia_spravy_ciest.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShowAllUsers(

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("email")
    @Expose
    var email: String,

    @SerializedName("created_at")
    @Expose
    var created_at: String,

    @SerializedName("role")
    @Expose
    var role: String

)
