package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import com.gtp01.group01.android.recipesmobileapp.shared.sources.RecipeManagementApiService
import retrofit2.Response
import javax.inject.Inject

class FavoriteRepository @Inject constructor(private val recipeManagementApiService: RecipeManagementApiService,private val sharedPreferences: SharedPreferences) {

    // Function to fetch favorite recipes by providing a hardcoded user ID
    suspend fun getFavoriteRecipesByUserId(userId: Int): List<Recipe> {
        return try {
            // Make a network call to fetch favorite recipes using the provided user ID
            val response: Response<List<Recipe>> = recipeManagementApiService.getFavorites(userId)

            if (response.isSuccessful) {
                // Extract the list of favorite recipes from the response body
                val recipes = response.body() ?: emptyList()

                // Decode images from binary data and update bitmap property of each recipe
                recipes.forEach { recipe ->
                    recipe.photo?.let { binaryData ->
                        recipe.bitmap = decodeImage(binaryData as String)
                    }
                }

                recipes // Return the updated list of recipes
            } else {
                // Handle unsuccessful response, e.g., server error
                emptyList()
            }
        } catch (e: Exception) {
            // Handle exceptions, logging, or return an empty list
            emptyList()
        }
    }

    // Decode image function
    private fun decodeImage(imageData: String): Bitmap? {
        return try {
            val decodedBytes = android.util.Base64.decode(imageData, android.util.Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            null
        }
    }
    private val gson = Gson()
    private val type = object : TypeToken<List<Int>>() {}.type // Change to list of recipe IDs



    fun getFavorites(userId: Int): List<Int> {
        val json = sharedPreferences.getString("favorites_$userId", "")
        return if (json.isNullOrEmpty()) {
            emptyList()
        } else {
            gson.fromJson(json, type)
        }
    }

    fun addFavorite(userId: Int, recipeId: Int) {
        val favorites = getFavorites(userId).toMutableList()
        if (!favorites.contains(recipeId)) {
            favorites.add(recipeId)
            saveFavorites(userId, favorites)
        }
    }

    fun removeFavorite(userId: Int, recipeId: Int) {
        val favorites = getFavorites(userId).toMutableList()
        favorites.remove(recipeId)
        saveFavorites(userId, favorites)
    }

    private fun saveFavorites(userId: Int, favorites: List<Int>) {
        val json = gson.toJson(favorites)
        sharedPreferences.edit().putString("favorites_$userId", json).apply()
        }


}