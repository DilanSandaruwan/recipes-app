package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import com.gtp01.group01.android.recipesmobileapp.shared.sources.AuthApiService
import javax.inject.Inject
/**
 * Repository responsible for fetching recipe details from the network.
 * @param authApiService The authentication API service used for making network requests.
 */
class ViewRecipeRepository @Inject constructor(private val authApiService: AuthApiService) {
    /**
     * Fetches recipe details for the specified user and recipe ID.
     * @param idLoggedUser The ID of the logged-in user.
     * @param idrecipe The ID of the recipe to fetch details for.
     * @return Recipe object containing the details of the recipe, or null if the request fails.
     */
    suspend fun getRecipeDetail(idLoggedUser: Int, idrecipe: Int):  Recipe? {
        return try {

            val response = authApiService.getRecipeDetail(idLoggedUser, idrecipe)
            if (response.isSuccessful) {
                val recipe = response.body()
                recipe?.let { item ->
                    val bitmap = if (item.photo != null) {
                        decodeImage(item.photo)
                    } else {
                        null
                    }
                    item.bitmap = bitmap
                }
                recipe
            } else {
                // Handle error response, log or throw exception

                null
            }
        } catch (e: Exception) {
            // Handle exception, log or throw

            null
        }
    }
    /**
     * Decodes Base64 encoded image data into a Bitmap object.
     * @param imageData Base64 encoded image data.
     * @return Decoded Bitmap object representing the image, or null if decoding fails.
     */
    private fun decodeImage(imageData: String): Bitmap? {
        return try {
            val decodedBytes = android.util.Base64.decode(imageData, android.util.Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {

            null
        }
    }

}