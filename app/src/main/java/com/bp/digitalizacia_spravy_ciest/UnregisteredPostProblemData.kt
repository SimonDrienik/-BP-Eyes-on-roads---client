package com.bp.digitalizacia_spravy_ciest

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class UnregisteredPostProblemData (
    @SerializedName("id") val id: BigInteger?,
    @SerializedName("poloha") val poloha: String?,
    @SerializedName("popis_problemu") val popis_problemu: String?,
    @SerializedName("kategoria_problemu") val kategoria_problemu: String?,
    @SerializedName("stav_problemu") val stav_problemu: String?
)
