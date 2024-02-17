package com.gtp01.group01.android.recipesmobileapp.shared.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a food category.
 *
 * @property idfoodCategory The unique identifier of the food category.
 * @property categoryName The name of the food category.
 */
@Parcelize
data class FoodCategory(
    val idfoodCategory: Int,
    val categoryName: String
) : Parcelable
