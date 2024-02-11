package com.gtp01.group01.android.recipesmobileapp.data

data class Recipe (
    val idrecipe: Long,
    val owner: AuthUser,
    val recipeName: String,
    val ingredients: String,
    val instruction: String,
    val preparationTime: Float,
    val calory: Float,
    val protein: Float,
    val carbs: Float,
    val serving: Int,
    val photo: String?, // Nullable if photo is optional
    val categories: List<FoodCategory>,
    val isActive: Int, // Change type to Int
    val likeCount: Int,
    val hasLike: Boolean,
    val hasFavorite: Boolean
)