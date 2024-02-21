package com.gtp01.group01.android.recipesmobileapp.repository

import com.gtp01.group01.android.recipesmobileapp.shared.models.NutritionModel
import com.gtp01.group01.android.recipesmobileapp.sources.RecipeManagementApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repository for managing recipe-related data.
 *
 * @property recipeManagementApiService The API service for recipe management.
 */
class RecipeManagementRepository @Inject constructor(
    private val recipeManagementApiService: RecipeManagementApiService
) {
    /**
     * Retrieves nutrition information for the provided ingredients from the remote service.
     *
     * @param ingredients The ingredients for which to retrieve nutrition information.
     * @return A list of [NutritionModel] objects representing the retrieved nutrition information, or null if an error occurs.
     */
    suspend fun getNutritionsRepo(ingredients: String): List<NutritionModel>? {

        return withContext(Dispatchers.IO) {
            return@withContext getNutritionsResponseFromRemoteService(ingredients)
        }

    }

    /**
     * Retrieves nutrition information from the remote service based on the provided ingredients.
     *
     * @param ingredients The ingredients for which to retrieve nutrition information.
     * @return A list of [NutritionModel] objects representing the retrieved nutrition information, or null if an error occurs.
     */
    private suspend fun getNutritionsResponseFromRemoteService(ingredients: String): List<NutritionModel>? {
        val response = recipeManagementApiService.getNutritions(ingredients)
        return if (response.isSuccessful) {
            response.body()
        } else {
            emptyList()
        }
    }

}