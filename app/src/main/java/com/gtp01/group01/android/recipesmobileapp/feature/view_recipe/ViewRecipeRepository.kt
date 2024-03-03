package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.ETC1.decodeImage
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
                val recipe = response.body()
                recipe?.let { item ->
                    val bitmap = if (item.photo != null) {
                        Log.d(TAG, "Fetching image for recipe: ${item.recipeName}")
                        decodeImage(item.photo)
                    } else {
                        Log.d(TAG, "Recipe ${item.recipeName} does not have an image.")
                        null
                    }
                    item.bitmap = bitmap
                    Log.d(TAG, "Recipe details fetched successfully: $recipe")
                }
                recipe
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
    private fun decodeImage(imageData: String): Bitmap? {
        return try {
            val decodedBytes = android.util.Base64.decode(imageData, android.util.Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            Log.e(TAG, "Error decoding image: ${e.message}")
            null
        }
    }
    companion object {
        private const val TAG = "ViewRecipeRepository"
    }
}