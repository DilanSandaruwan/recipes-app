package com.gtp01.group01.android.recipesmobileapp.shared.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Represents a food category.
 *
 * @property idfoodCategory The unique identifier of the food category.
 * @property categoryName The name of the food category.
 */
data class FoodCategoryApp(
    @SerializedName("idfoodCategory") val idFoodCategory: Int,
    @SerializedName("categoryName") val categoryName: String,
    var categoryImageId: Int,
    var isSelected: Boolean = false,
) : Serializable
