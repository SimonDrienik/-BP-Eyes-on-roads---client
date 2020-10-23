package com.bp.digitalizacia_spravy_ciest

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Problems(
    @SerializedName("problem_id")
    val id: Int? = null,

    @SerializedName("poloha")
    val poloha: String? = null,

    @SerializedName("popis_problemu")
    val popis: String? = null
) : Serializable


