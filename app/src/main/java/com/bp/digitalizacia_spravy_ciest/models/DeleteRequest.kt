package com.bp.digitalizacia_spravy_ciest.models

import com.google.gson.annotations.SerializedName

data class DeleteRequest(
    @SerializedName("AuthToken")
    var authToken: String,

    @SerializedName("problemID")
    var problemID: Int
)
