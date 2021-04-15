package com.bp.digitalizacia_spravy_ciest.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DeleteAccount(
    @SerializedName("email")
    @Expose
    var email: String = "",

    @SerializedName("AuthToken")
    @Expose
    var AuthToken: String = ""
)
