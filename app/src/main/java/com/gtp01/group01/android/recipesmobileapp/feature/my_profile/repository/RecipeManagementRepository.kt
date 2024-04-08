package com.gtp01.group01.android.recipesmobileapp.feature.my_profile.repository

import android.util.Log
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantResponseCode
import com.gtp01.group01.android.recipesmobileapp.shared.common.Result
import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategory
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import com.gtp01.group01.android.recipesmobileapp.shared.models.NutritionModel
import com.gtp01.group01.android.recipesmobileapp.shared.sources.RecipeManagementApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * Repository for managing recipe-related data.
 *
 * @property recipeManagementApiService The API service for recipe management.
 */
class RecipeManagementRepository @Inject constructor(
    private val recipeManagementApiService: RecipeManagementApiService
) {
    // Logging tag for this class
    private val TAG = this::class.java.simpleName

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
     * Fetches recipes filtered by duration from the remote server asynchronously.
     *
     * @param loggedUserId The ID of the logged-in user.
     * @param maxDuration The cooking time of the recipes to filter.
     * @return A flow emitting [Result] objects containing either a list of filtered recipes or an error.
     */
    suspend fun filterRecipesByDuration(
        loggedUserId: Int,
        maxDuration: Int
    ): Flow<Result<List<Recipe>>> {
        return withContext(Dispatchers.IO) {
            return@withContext flow {
                try {
                    val response = recipeManagementApiService.filterRecipesByDuration(
                        loggedUserId,
                        maxDuration
                    )
                    if (response.isSuccessful) {
                        // Emit a successful result with the response body
                        emit(Result.Success(response.body() ?: emptyList()))
                    } else {
                        // Emit a failure result with the HTTP error code
                        emit(Result.Failure(response.code().toString()))
                        val errorMessage =
                            "Failed to filter recipes for user $loggedUserId, duration $maxDuration: ${
                                response.errorBody().toString()
                            }"
                        Log.e(TAG, errorMessage)
                    }
                } catch (ex: IOException) {
                    // Emit a failure result for network errors
                    emit(Result.Failure(ConstantResponseCode.IOEXCEPTION))
                    val errorMessage = "Network error occurred: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                } catch (ex: SocketTimeoutException) {
                    // Emit a failure result for connection timeout errors
                    emit(Result.Failure(ConstantResponseCode.TIMEOUT_EXCEPTION))
                    val errorMessage = "Connection timed out: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                } catch (ex: Exception) {
                    // Emit a failure result for unexpected errors
                    emit(Result.Failure(ConstantResponseCode.EXCEPTION))
                    val errorMessage = "An unexpected error occurred: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                }
            }
        }
    }

    /**
     * Fetches recipes filtered by calorie from the remote server asynchronously.
     *
     * @param loggedUserId The ID of the logged-in user.
     * @param maxCalorie The calorie count of the recipes to filter.
     * @return A flow emitting [Result] objects containing either a list of filtered recipes or an error.
     */
    suspend fun filterRecipesByCalorie(
        loggedUserId: Int,
        maxCalorie: Int
    ): Flow<Result<List<Recipe>>> {
        return withContext(Dispatchers.IO) {
            return@withContext flow {
                try {
                    val response = recipeManagementApiService.filterRecipesByCalorie(
                        loggedUserId,
                        maxCalorie
                    )
                    if (response.isSuccessful) {
                        // Emit a successful result with the response body
                        emit(Result.Success(response.body() ?: emptyList()))
                    } else {
                        // Emit a failure result with the HTTP error code
                        emit(Result.Failure(response.code().toString()))
                        val errorMessage =
                            "Failed to filter recipes for user $loggedUserId, calorie $maxCalorie: ${
                                response.errorBody().toString()
                            }"
                        Log.e(TAG, errorMessage)
                    }
                } catch (ex: IOException) {
                    // Emit a failure result for network errors
                    emit(Result.Failure(ConstantResponseCode.IOEXCEPTION))
                    val errorMessage = "Network error occurred: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                } catch (ex: SocketTimeoutException) {
                    // Emit a failure result for connection timeout errors
                    emit(Result.Failure(ConstantResponseCode.TIMEOUT_EXCEPTION))
                    val errorMessage = "Connection timed out: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                } catch (ex: Exception) {
                    // Emit a failure result for unexpected errors
                    emit(Result.Failure(ConstantResponseCode.EXCEPTION))
                    val errorMessage = "An unexpected error occurred: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                }
            }
        }
    }

    /**
     * Fetches recipes filtered by name from the remote server asynchronously.
     *
     * @param loggedUserId The ID of the logged-in user.
     * @param recipeName The name of the recipes to filter.
     * @return A flow emitting [Result] objects containing either a list of filtered recipes or an error.
     */
    suspend fun filterRecipesByName(
        loggedUserId: Int,
        recipeName: String
    ): Flow<Result<List<Recipe>>> {
        return withContext(Dispatchers.IO) {
            return@withContext flow {
                emit(Result.Loading) // Indicate loading state
                try {
                    val response = recipeManagementApiService.filterRecipesByName(
                        loggedUserId,
                        recipeName
                    )
                    if (response.isSuccessful) {
                        // Emit a successful result with the response body
                        emit(Result.Success(response.body() ?: emptyList()))
                    } else {
                        // Emit a failure result with the HTTP error code
                        emit(Result.Failure(response.code().toString()))
                        val errorMessage =
                            "Failed to filter recipes for user $loggedUserId, name $recipeName: ${
                                response.errorBody().toString()
                            }"
                        Log.e(TAG, errorMessage)
                    }
                } catch (ex: IOException) {
                    // Emit a failure result for network errors
                    emit(Result.Failure(ConstantResponseCode.IOEXCEPTION))
                    val errorMessage = "Network error occurred: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                } catch (ex: SocketTimeoutException) {
                    // Emit a failure result for connection timeout errors
                    emit(Result.Failure(ConstantResponseCode.TIMEOUT_EXCEPTION))
                    val errorMessage = "Connection timed out: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                } catch (ex: Exception) {
                    // Emit a failure result for unexpected errors
                    emit(Result.Failure(ConstantResponseCode.EXCEPTION))
                    val errorMessage = "An unexpected error occurred: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                }
            }
        }
    }

    /**
     * Fetches recipes filtered by category from the remote server asynchronously.
     *
     * @param loggedUserId The ID of the logged-in user.
     * @param categoryId The category of the recipes to filter.
     * @return A flow emitting [Result] objects containing either a list of filtered recipes or an error.
     */
    suspend fun filterRecipesByCategory(
        loggedUserId: Int,
        categoryId: Int
    ): Flow<Result<List<Recipe>>> {
        return withContext(Dispatchers.IO) {
            return@withContext flow {
                emit(Result.Loading) // Indicate loading state
                try {
                    val response = recipeManagementApiService.filterRecipesByCategory(
                        idLoggedUser = loggedUserId,
                        categoryId = categoryId
                    )
                    if (response.isSuccessful) {
                        // Emit a successful result with the response body
                        emit(Result.Success(response.body() ?: emptyList()))
                    } else {
                        // Emit a failure result with the HTTP error code
                        emit(Result.Failure(response.code().toString()))
                        val errorMessage =
                            "Failed to filter recipes for user $loggedUserId, category $categoryId: ${
                                response.errorBody().toString()
                            }"
                        Log.e(TAG, errorMessage)
                    }
                } catch (ex: IOException) {
                    // Emit a failure result for network errors
                    emit(Result.Failure(ConstantResponseCode.IOEXCEPTION))
                    val errorMessage = "Network error occurred: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                } catch (ex: SocketTimeoutException) {
                    // Emit a failure result for connection timeout errors
                    emit(Result.Failure(ConstantResponseCode.TIMEOUT_EXCEPTION))
                    val errorMessage = "Connection timed out: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                } catch (ex: Exception) {
                    // Emit a failure result for unexpected errors
                    emit(Result.Failure(ConstantResponseCode.EXCEPTION))
                    val errorMessage = "An unexpected error occurred: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                }
            }
        }
    }

    /**
     * Saves a like to a recipe asynchronously.
     *
     * @param loggedUserId The ID of the logged-in user.
     * @param recipeId The RECIPE ID of the recipe.
     * @return A flow emitting [Boolean] value to confirm whether like was saved in backend.
     */
    suspend fun likeRecipe(
        loggedUserId: Int,
        recipeId: Int
    ): Flow<Result<Boolean>> {
        return withContext(Dispatchers.IO) {
            return@withContext flow {
                emit(Result.Loading) // Indicate loading state
                try {
                    val response = recipeManagementApiService.likeRecipe(
                        idLoggedUser = loggedUserId,
                        recipeId = recipeId
                    )
                    if (response.isSuccessful) {
                        // Emit a successful result with value TRUE
                        emit(Result.Success(true))
                    } else {
                        // Emit a failure result with the HTTP error code
                        emit(Result.Failure(response.code().toString()))
                        val errorMessage =
                            "Failed to like a recipe for user $loggedUserId, recipe $recipeId: ${
                                response.errorBody()?.string()
                            }"
                        Log.e(TAG, errorMessage)
                    }
                } catch (ex: IOException) {
                    // Emit a failure result for network errors
                    emit(Result.Failure(ConstantResponseCode.IOEXCEPTION))
                    val errorMessage = "Network error occurred: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                } catch (ex: SocketTimeoutException) {
                    // Emit a failure result for connection timeout errors
                    emit(Result.Failure(ConstantResponseCode.TIMEOUT_EXCEPTION))
                    val errorMessage = "Connection timed out: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                } catch (ex: Exception) {
                    // Emit a failure result for unexpected errors
                    emit(Result.Failure(ConstantResponseCode.EXCEPTION))
                    val errorMessage = "An unexpected error occurred: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                }
            }
        }
    }

    /**
     * Removes a like from a recipe asynchronously.
     *
     * @param loggedUserId The ID of the logged-in user.
     * @param recipeId The RECIPE ID of the recipe.
     * @return A flow emitting [Boolean] value to confirm whether like was removed from backend.
     */
    suspend fun removeLikeRecipe(
        loggedUserId: Int,
        recipeId: Int
    ): Flow<Result<Boolean>> {
        return withContext(Dispatchers.IO) {
            return@withContext flow {
                emit(Result.Loading) // Indicate loading state
                try {
                    val response = recipeManagementApiService.removeLikeRecipe(
                        idLoggedUser = loggedUserId,
                        recipeId = recipeId
                    )
                    if (response.isSuccessful) {
                        // Emit a successful result with value TRUE
                        emit(Result.Success(true))
                    } else {
                        // Emit a failure result with the HTTP error code
                        emit(Result.Failure(response.code().toString()))
                        val errorMessage =
                            "Failed to dislike a recipe for user $loggedUserId, recipe $recipeId: ${
                                response.errorBody()?.string()
                            }"
                        Log.e(TAG, errorMessage)
                    }
                } catch (ex: IOException) {
                    // Emit a failure result for network errors
                    emit(Result.Failure(ConstantResponseCode.IOEXCEPTION))
                    val errorMessage = "Network error occurred: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                } catch (ex: SocketTimeoutException) {
                    // Emit a failure result for connection timeout errors
                    emit(Result.Failure(ConstantResponseCode.TIMEOUT_EXCEPTION))
                    val errorMessage = "Connection timed out: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                } catch (ex: Exception) {
                    // Emit a failure result for unexpected errors
                    emit(Result.Failure(ConstantResponseCode.EXCEPTION))
                    val errorMessage = "An unexpected error occurred: ${ex.message}"
                    Log.e(TAG, errorMessage, ex)
                }
            }
        }
    }
}