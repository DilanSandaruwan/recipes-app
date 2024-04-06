package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites

import android.content.SharedPreferences
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
                response.body() ?: emptyList()
            } else {
                // Handle unsuccessful response, e.g., server error
                emptyList()
            }
        } catch (e: Exception) {
            // Handle exceptions, logging, or return an empty list
            emptyList()
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
