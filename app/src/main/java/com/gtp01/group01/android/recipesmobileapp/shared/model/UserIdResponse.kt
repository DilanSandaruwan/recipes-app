package com.gtp01.group01.android.recipesmobileapp.shared.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
/**
 * Data class representing the response containing user details fetched by the API.
 *
 * @property iduser The user ID.
 * @property email The email address of the user.
 * @property fullname The full name of the user.
 * @property preferCategories The list of preferred categories of the user.
 * @property preferDuration The preferred duration of the user.
 * @property preferCalory The preferred calorie intake of the user.
 */
data class UserIdResponse(
    @SerializedName("iduser") val iduser: Int,
    @SerializedName("email") val email: String,
    @SerializedName("fullname") val fullname: String,
    @SerializedName("preferCategories") val preferCategories: List<String>,
    @SerializedName("preferDuration") val preferDuration: Int,
    @SerializedName("preferCalory") val preferCalory: Int
): Serializable
