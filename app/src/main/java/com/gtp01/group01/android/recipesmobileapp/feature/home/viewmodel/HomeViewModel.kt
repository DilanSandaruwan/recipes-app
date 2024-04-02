package com.gtp01.group01.android.recipesmobileapp.feature.home.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantResponseCode.EXCEPTION
import com.gtp01.group01.android.recipesmobileapp.feature.my_profile.repository.RecipeManagementRepository
import com.gtp01.group01.android.recipesmobileapp.shared.common.Result
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import com.gtp01.group01.android.recipesmobileapp.shared.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Home screen.
 * Retrieves and manages data related to recipes.
 *
 * @property recipeManagementRepository The repository for managing recipe data.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val recipeManagementRepository: RecipeManagementRepository) :
    ViewModel() {
    // Logging tag for this class
    private val TAG = this::class.java.simpleName

    // LiveData for holding logged-in user
    private val _user = MutableLiveData<User?>(null)
    val user: LiveData<User?> = _user

    // StateFlow for holding time-based recipe list state
    private val _timeBasedRecipeListState = MutableStateFlow<Result<List<Recipe>>>(Result.Loading)
    val timeBasedRecipeListState: StateFlow<Result<List<Recipe>>> = _timeBasedRecipeListState

    // StateFlow for holding calorie-based recipe list state
    private val _calorieBasedRecipeListState =
        MutableStateFlow<Result<List<Recipe>>>(Result.Loading)
    val calorieBasedRecipeListState: StateFlow<Result<List<Recipe>>> = _calorieBasedRecipeListState


    // MutableState for holding search keyword
    var searchKeyword by mutableStateOf("")

    /**
     * Fetches recipes filtered by cooking time asynchronously.
     *
     * @param loggedUserId The ID of the logged-in user.
     * @param maxDuration The maximum cooking time of the recipes to filter.
     */
    fun filterRecipesByDuration(loggedUserId: Int, maxDuration: Int) {
        viewModelScope.launch {
            try {
                _timeBasedRecipeListState.value = Result.Loading
                recipeManagementRepository.filterRecipesByDuration(loggedUserId, maxDuration)
                    .flowOn(Dispatchers.IO)
                    .collect { recipeList ->
                        _timeBasedRecipeListState.value = recipeList
                    }
            } catch (ex: Exception) {
                _timeBasedRecipeListState.value = Result.Failure(EXCEPTION)
                Log.e(TAG, ex.message ?: "An error occurred", ex)
            }
        }
    }

    /**
     * Fetches recipes filtered by calorie count asynchronously.
     *
     * @param loggedUserId The ID of the logged-in user.
     * @param maxCalorie The maximum calorie count of the recipes to filter.
     */
    fun filterRecipesByCalorie(loggedUserId: Int, maxCalorie: Int) {
        viewModelScope.launch {
            try {
                _calorieBasedRecipeListState.value = Result.Loading
                recipeManagementRepository.filterRecipesByCalorie(loggedUserId, maxCalorie)
                    .flowOn(Dispatchers.IO)
                    .collect { recipeList ->
                        _calorieBasedRecipeListState.value = recipeList
                    }
            } catch (ex: Exception) {
                _calorieBasedRecipeListState.value = Result.Failure(EXCEPTION)
                Log.e(TAG, ex.message ?: "An error occurred", ex)
            }
        }
    }

    /**
     * Updates the search keyword.
     *
     * @param enteredKeyword The new search keyword entered by the user.
     */
    fun updateSearchKeyword(enteredKeyword: String) {
        searchKeyword = enteredKeyword
    }

    /**
     * Decodes Base64 encoded image data to Bitmap.
     *
     * @param imageValue The Base64 encoded string representing the image.
     * @return The decoded Bitmap image.
     */
    fun decodeImageToBitmap(imageValue: String): Bitmap? {
        return try {
            // Decode the Base64 string into a byte array.
            val decodedBytes: ByteArray =
                android.util.Base64.decode(imageValue, android.util.Base64.DEFAULT)

            // Attempt to create a Bitmap from the decoded bytes.
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            Log.e(TAG, "Error decoding image from Base64 string: $e")
            null
        }
    }
}