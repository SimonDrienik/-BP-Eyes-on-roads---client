package com.bp.digitalizacia_spravy_ciest.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Spinners(
    @SerializedName("zamestnanci")
    @Expose
    var zamestnanci: List<String> ,

    @SerializedName("priority")
    @Expose
    var priority: List<String>,

    @SerializedName("kategorie")
    @Expose
    var kategorie: List<String>,

    @SerializedName("stavy_problemu")
    @Expose
    var stavy_problemu: List<String>,

    @SerializedName("stavy_riesenia")
    @Expose
    var stavy_riesenia: List<String>,

    @SerializedName("vozidla")
    @Expose
    var vozidla: List<String>
)