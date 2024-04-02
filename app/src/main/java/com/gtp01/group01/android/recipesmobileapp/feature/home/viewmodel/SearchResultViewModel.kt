package com.gtp01.group01.android.recipesmobileapp.feature.home.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantResponseCode
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
 * ViewModel for the Search result screen.
 * Retrieves data related to searched recipes.
 *
 * @property recipeManagementRepository The repository for managing recipe data.
 */
@HiltViewModel
class SearchResultViewModel @Inject constructor(private val recipeManagementRepository: RecipeManagementRepository) :
    ViewModel() {
    // Logging tag for this class
    private val TAG = this::class.java.simpleName

    // LiveData for holding logged-in user
    private val _user = MutableLiveData<User?>(null)
    val user: LiveData<User?> = _user

    // StateFlow for holding searched recipe list state
    private val _searchResultRecipeListState =
        MutableStateFlow<Result<List<Recipe>>>(Result.Loading)
    val searchResultRecipeListState: StateFlow<Result<List<Recipe>>> = _searchResultRecipeListState

    /**
     * Fetches recipes filtered by recipe name asynchronously.
     *
     * @param loggedUserId The ID of the logged-in user.
     * @param recipeName The name of the recipes to filter.
     */
    fun filterRecipesByName(loggedUserId: Int, recipeName: String) {
        viewModelScope.launch {
            try {
                _searchResultRecipeListState.value = Result.Loading
                recipeManagementRepository.filterRecipesByName(loggedUserId, recipeName.trim())
                    .flowOn(Dispatchers.IO)
                    .collect { recipeList ->
                        _searchResultRecipeListState.value = recipeList
                    }
            } catch (ex: Exception) {
                _searchResultRecipeListState.value = Result.Failure(ConstantResponseCode.EXCEPTION)
                Log.e(TAG, ex.message ?: "An error occurred", ex)
            }
        }
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