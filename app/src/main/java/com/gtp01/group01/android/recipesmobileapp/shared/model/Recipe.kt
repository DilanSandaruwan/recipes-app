package com.gtp01.group01.android.recipesmobileapp.shared.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
@Parcelize
data class Recipe(
    val idrecipe: Int,
    val owner: User,
    val recipeName: String,
    val ingredients: String,
    val instruction: String,
    val preparationTime: Int,
    val calory: Int,
    val protein: Int,
    val carbs: Int,
    val serving: Int,
    val photo: String?,
    val categories: List<FoodCategory>,
    val isActive: Int,
    val likeCount: Int,
    val hasLike: Boolean,
    val hasFavorite: Boolean
) : Parcelable

/**
 * Represents a list of recipes.
 *
 * @property recipes The list of recipes.
 */
@Parcelize
data class RecipeList(
    val recipes: List<Recipe>
) : Parcelable