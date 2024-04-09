package com.gtp01.group01.android.recipesmobileapp.feature.my_recipes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtp01.group01.android.recipesmobileapp.feature.my_profile.repository.RecipeManagementRepository
import com.gtp01.group01.android.recipesmobileapp.shared.common.Logger
import com.gtp01.group01.android.recipesmobileapp.shared.common.Result
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing recipe-related data for the My Recipes screen.
 * Holds information about logged-in user and retrieves recipes from the repository.
 */
@HiltViewModel
class MyRecipesViewModel @Inject constructor(
    private val recipeManagementRepository: RecipeManagementRepository,
    private val logger: Logger
) : ViewModel() {

    var userId: Int = 0

    /**
     * StateFlow holding the current result of fetching the user's recipes,
     * including loading, success, or failure states.
     */
    private val _myRecipesList = MutableStateFlow<Result<List<Recipe>>>(Result.Loading)
    val myRecipesList: StateFlow<Result<List<Recipe>>> = _myRecipesList

    /**
     * Initiates retrieval of recipes created by the specified user from the repository.
     * Handles potential errors and updates the [myRecipesList] with the result.
     *
     * @param idLoggedUser The ID of the user whose recipes to retrieve.
     */
    fun getMyRecipes(idLoggedUser: Int) {
        viewModelScope.launch {
            try {
                recipeManagementRepository.getMyRecipes(idLoggedUser)
                    .flowOn(Dispatchers.IO) // Handle data retrieval on a background thread
                    .collect { recipeList ->
                        _myRecipesList.value = recipeList
                    }
            } catch (ex: Exception) {
                _myRecipesList.value = Result.Failure("Exception")
                logger.error("MyRecipesViewModel", ex.message ?: "An error occurred", ex)
            }
        }
    }

    /**
     * Decodes a Base64 encoded image string into a Bitmap.
     *
     * @param imageValue The Base64 encoded image string.
     * @return The decoded Bitmap image, or null if decoding fails.
     */
    fun decodeImageToBitmap(imageValue: String): Bitmap? {
        return try {
            // Decode the Base64 string into a byte array.
            val decodedBytes: ByteArray =
                android.util.Base64.decode(imageValue, android.util.Base64.DEFAULT)

            // Attempt to create a Bitmap from the decoded bytes.
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            logger.error("MyRecipes", "Error decoding image from Base64 string: $e", e)
            null
        }
    }
}
