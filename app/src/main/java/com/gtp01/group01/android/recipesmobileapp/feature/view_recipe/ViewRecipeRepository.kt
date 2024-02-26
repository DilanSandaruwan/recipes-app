package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe

import android.util.Log
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import com.gtp01.group01.android.recipesmobileapp.shared.sources.AuthApiService
import javax.inject.Inject

class ViewRecipeRepository @Inject constructor(private val authApiService: AuthApiService) {

    suspend fun getRecipeDetail(idLoggedUser: Int, idrecipe: Int):  Recipe? {
        return try {
            Log.d(TAG, "Making network request to fetch recipe details for user id: $idLoggedUser, recipe name: $idrecipe")
            val response = authApiService.getRecipeDetail(idLoggedUser, idrecipe)
            if (response.isSuccessful) {
                val recipes = response.body()

                Log.d(TAG, "Recipe details fetched successfully: $recipes")
                recipes
            } else {
                // Handle error response, log or throw exception
                Log.e(TAG, "Failed to fetch recipe details. Error code: ${response.code()}, message: ${response.message()}")
                null
            }
        } catch (e: Exception) {
            // Handle exception, log or throw
            Log.e(TAG, "Error fetching recipe details: ${e.message}")
            null
        }
    }

    companion object {
        private const val TAG = "ViewRecipeRepository"
    }
}