package com.bp.digitalizacia_spravy_ciest.models

import com.google.gson.annotations.SerializedName

data class EditProblem(
    @SerializedName("zamestnanec")
    var zamestnanec: String ,

    @SerializedName("priorita")
    var priorita: String ,

    @SerializedName("kategoria")
    var kategoria: String ,

    @SerializedName("stavProblemu")
    var stavProblemu: String ,

    @SerializedName("stavRiesenia")
    var stavRiesenia: String ,

    @SerializedName("vozidlo")
    var vozidlo: String,

    @SerializedName("popisRiesenia")
    var popisRiesenia: String,

    @SerializedName("token")
    var token: String,

    @SerializedName("problemID")
    var problemID: Int,

    @SerializedName("verejne")
    var verejne: Int
)
