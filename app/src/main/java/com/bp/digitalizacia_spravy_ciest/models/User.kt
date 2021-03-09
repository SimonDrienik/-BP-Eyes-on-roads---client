package com.bp.digitalizacia_spravy_ciest.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    var id: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("rola_id")
    var rola_id: Int
) {
    companion object {
        @Volatile
        @JvmStatic
        private var INSTANCE: User? = null

        @JvmStatic
        @JvmOverloads
        fun getInstance(id: Int = 0, name: String = "", email: String = "", rola_id: Int = 0): User = INSTANCE ?: synchronized(this) {
            INSTANCE ?: User(id, name, email, rola_id).also { INSTANCE = it }
        }
    }
}

