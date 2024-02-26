package com.gtp01.group01.android.recipesmobileapp.shared.model

import com.google.gson.annotations.SerializedName

class FoodCategory (
    @SerializedName("idfoodCategory") val idFoodCategory: Int,
    @SerializedName("categoryName") val categoryName: String
)