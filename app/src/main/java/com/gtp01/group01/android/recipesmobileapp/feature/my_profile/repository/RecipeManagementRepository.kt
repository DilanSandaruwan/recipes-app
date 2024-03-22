package com.gtp01.group01.android.recipesmobileapp.feature.my_profile.repository

import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategory
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import com.gtp01.group01.android.recipesmobileapp.shared.models.NutritionModel
import com.gtp01.group01.android.recipesmobileapp.shared.sources.RecipeManagementApiService
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

    suspend fun saveNewRecipe(idLoggedUser: Int, recipe: Recipe): Recipe? {
        return withContext(Dispatchers.IO) {
            return@withContext saveNewRecipeResponseFromRemoteService(idLoggedUser, recipe)
        }
    }

    private suspend fun saveNewRecipeResponseFromRemoteService(
        idLoggedUser: Int,
        recipe: Recipe
    ): Recipe? {
        val response = recipeManagementApiService.saveNewRecipe(idLoggedUser, recipe)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun getCategoryList(): List<FoodCategory>? {
        return withContext(Dispatchers.IO) {
            return@withContext getCategoryListFromRemoteService()
        }

    }

    private suspend fun getCategoryListFromRemoteService(): List<FoodCategory>? {
        val response = recipeManagementApiService.getCategoryList()
        return if (response.isSuccessful) {
            response.body()
        } else {
            emptyList()
        }
    }
    /**
     * Retrieves a list of active recipes filtered by preparation time duration.
     *
     * @return A list of [Recipe] containing the filtered list of [Recipe].
     */
    suspend fun filterRecipesByDuration(idLoggedUser: Int, maxduration: Int): List<Recipe> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val response = recipeManagementApiService.filterRecipesByDuration(idLoggedUser, maxduration)
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    emptyList<Recipe>()
                }
            } catch (e: Exception) {
                emptyList<Recipe>()
            }
        }
    }
}