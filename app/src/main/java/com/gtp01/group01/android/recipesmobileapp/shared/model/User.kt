package com.gtp01.group01.android.recipesmobileapp.shared.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Represents a user.
 *
 * @property iduser The unique identifier of the user.
 * @property email The email address of the user.
 * @property fullname The full name of the user.
 * @property preferCategories The list of food categories preferred by the user.
 * @property preferDuration The duration in minutes for recipe preparation preferred by the user.
 * @property preferCalory The calorie intake preferred by the user.
 */
data class User(
    @SerializedName("iduser") val idUser: Int,
    @SerializedName("email") val email: String? = null,
    @SerializedName("fullname") val fullName: String? = null,
    @SerializedName("preferCategories") val preferCategories: List<FoodCategoryApp> = emptyList(),
    @SerializedName("preferDuration") val preferDuration: Int = 0,
    @SerializedName("preferCalory") val preferCalorie: Int = 0,
):Serializable