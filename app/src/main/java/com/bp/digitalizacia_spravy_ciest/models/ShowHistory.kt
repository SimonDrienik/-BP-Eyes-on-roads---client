package com.bp.digitalizacia_spravy_ciest.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShowHistory(
    @SerializedName("meno")
    @Expose
    var name: String,

    @SerializedName("date")
    @Expose
    var created_at: String,

    @SerializedName("user")
    @Expose
    var user: String
    )
