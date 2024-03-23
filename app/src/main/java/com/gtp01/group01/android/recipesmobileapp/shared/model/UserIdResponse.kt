package com.gtp01.group01.android.recipesmobileapp.shared.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserIdResponse(
    @SerializedName("iduser") val iduser: Int,
    @SerializedName("email") val email: String,
    @SerializedName("fullname") val fullname: String,
    @SerializedName("preferCategories") val preferCategories: List<String>,
    @SerializedName("preferDuration") val preferDuration: Int,
    @SerializedName("preferCalory") val preferCalory: Int
): Serializable
