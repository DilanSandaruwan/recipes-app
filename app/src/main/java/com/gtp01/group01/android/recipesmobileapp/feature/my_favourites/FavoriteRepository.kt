package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import javax.inject.Inject

class FavoriteRepository @Inject constructor(private val context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
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

    fun getFavoriteRecipes(): LiveData<List<Recipe>> {
        val favoritesLiveData = MutableLiveData<List<Recipe>>()
        val userId = 10 // Replace with actual logged-in user ID
        val favoriteRecipeIds = getFavorites(userId)
        // Fetch recipes from your data source using the recipe IDs
        // Here, I'm assuming you have a method to fetch recipes by ID
        // Replace RecipeDataSource with your actual data source class
       // val recipes = RecipeDataSource.getRecipesByIds(favoriteRecipeIds)
       // favoritesLiveData.value = recipes
        return favoritesLiveData
    }
}
