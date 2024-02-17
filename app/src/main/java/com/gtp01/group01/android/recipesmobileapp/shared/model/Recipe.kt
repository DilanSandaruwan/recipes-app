package com.gtp01.group01.android.recipesmobileapp.shared.model

import com.google.gson.annotations.SerializedName

/**
 * Represents a recipe.
 *
 * @property idrecipe The unique identifier of the recipe.
 * @property owner The user who wrote the recipe.
 * @property recipeName The name of the recipe.
 * @property ingredients The list of ingredients required for the recipe.
 * @property instruction The step-by-step instructions for preparing the recipe.
 * @property preparationTime The time required to prepare the recipe in minutes.
 * @property calory The calorie count of the recipe.
 * @property protein The protein count of the recipe.
 * @property carbs The carbohydrate count of the recipe.
 * @property serving The number of servings the recipe yields.
 * @property photo The photo of the prepared recipe.
 * @property categories The list of food categories to which the recipe belongs.
 * @property isActive The status indicating whether the recipe is active or disabled. 1= active, 0= disabled.
 * @property likeCount The number of likes the recipe has received.
 * @property hasLike Indicates whether the current logged in user has liked the recipe. Return true if user has liked the recipe.
 * @property hasFavorite Indicates whether the current logged in user has marked the recipe as favorite. Return true if user has marked as favorite.
 */

data class Recipe(
    @SerializedName("idrecipe") val idRecipe: Int,
    @SerializedName("owner") val owner: User,
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
    @SerializedName("hasFavorite") val hasFavorite: Boolean
)

/**
 * Represents a list of recipes.
 *
 * @property recipes The list of recipes.
 */

data class RecipeList(
    @SerializedName("recipes") val recipes: List<Recipe>
)