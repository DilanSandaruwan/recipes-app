package com.gtp01.group01.android.recipesmobileapp.shared.model

import com.google.gson.annotations.SerializedName

/**
 * Represents a food category.
 *
 * @property idfoodCategory The unique identifier of the food category.
 * @property categoryName The name of the food category.
 */
data class FoodCategory(
    @SerializedName("idfoodCategory") val idFoodCategory: Int,
    @SerializedName("categoryName") val categoryName: String
)
