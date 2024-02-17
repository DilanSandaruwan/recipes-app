package com.gtp01.group01.android.recipesmobileapp.shared.model

import com.google.gson.annotations.SerializedName

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
    @SerializedName("email") val email: String,
    @SerializedName("fullname") val fullName: String,
    @SerializedName("preferCategories") val preferCategories: List<FoodCategory>,
    @SerializedName("preferDuration") val preferDuration: Int,
    @SerializedName("preferCalory") val preferCalorie: Int
)