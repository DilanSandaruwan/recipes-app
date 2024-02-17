package com.gtp01.group01.android.recipesmobileapp.shared.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
@Parcelize
data class User(
    val iduser: Int,
    val email: String,
    val fullname: String,
    val preferCategories: List<FoodCategory>,
    val preferDuration: Int,
    val preferCalory: Int
) : Parcelable