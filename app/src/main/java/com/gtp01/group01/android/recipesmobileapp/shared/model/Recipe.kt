package com.gtp01.group01.android.recipesmobileapp.shared.model

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
/**
 * Data class representing a recipe.
 *
 * @property idRecipe The ID of the recipe.
 * @property owner The owner of the recipe, represented by an [AuthUser] object.
 * @property recipeName The name of the recipe.
 * @property ingredients The ingredients required for the recipe.
 * @property instruction The instructions to prepare the recipe.
 * @property preparationTime The preparation time in minutes.
 * @property calorie The calorie content of the recipe.
 * @property protein The protein content of the recipe.
 * @property carbs The carbohydrate content of the recipe.
 * @property serving The serving size of the recipe.
 * @property photo The base64 encoded string representing the photo of the recipe.
 * @property categories The list of food categories the recipe belongs to.
 * @property isActive The status of the recipe.
 * @property likeCount The number of likes received for the recipe.
 * @property hasLike Indicates whether the recipe has been liked.
 * @property hasFavorite Indicates whether the recipe has been marked as favorite.
 * @property bitmap The decoded image bitmap of the recipe photo.
 */
data class Recipe(
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