package com.bp.digitalizacia_spravy_ciest.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Imgs(
    @SerializedName("URLproblem")
    @Expose
    var urlProblem: String,

    @SerializedName("URLriesenie")
    @Expose
    var urlRiesenie: String
)
