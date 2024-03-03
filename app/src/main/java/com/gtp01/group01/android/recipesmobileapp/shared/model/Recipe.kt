package com.gtp01.group01.android.recipesmobileapp.shared.model

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class Recipe (
    @SerializedName("idrecipe") val idRecipe: Int,
    @SerializedName("owner") val owner: AuthUser,
    @SerializedName("recipeName") val recipeName: String,
    @SerializedName("ingredients") val ingredients: String,
    @SerializedName("instruction") val instruction: String,
    @SerializedName("preparationTime") val preparationTime: Int,
    @SerializedName("calory") val calorie: Int,
    @SerializedName("protein") val protein: Int,
    @SerializedName("carbs") val carbs: Int,
    @SerializedName("serving") val serving: Int,
    @SerializedName("photo") val photo: String?,
    @SerializedName("categories") val categories: List<FoodCategory>,
    @SerializedName("isActive") val isActive: Int,
    @SerializedName("likeCount") val likeCount: Int,
    @SerializedName("hasLike") val hasLike: Boolean,
    @SerializedName("hasFavorite") val hasFavorite: Boolean,
    var bitmap: Bitmap? = null // Add a property to hold the decoded image bitmap
)