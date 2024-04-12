package com.gtp01.group01.android.recipesmobileapp.feature.home.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
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
import com.gtp01.group01.android.recipesmobileapp.shared.common.viewmodel.SharedViewModel
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
 * ViewModel responsible for managing data related to recipes on the Home screen.
 *
 * This ViewModel provides functionality to fetch and manage recipes based on various criteria,
 * such as cooking time, calorie count, and user preferences. It also handles network connectivity
 * and user input for searching recipes.
 *
 * @property recipeManagementRepository The repository for fetching recipe data.
 * @property connectivityManager The manager for network connectivity monitoring.
 * @property sharedViewModel The shared view model holding logged-in user information.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    private val recipeManagementRepository: RecipeManagementRepository,
    private val sharedViewModel: SharedViewModel,
    private val networkRequest: NetworkRequest
) : ViewModel() {
    // Logging tag for this class
    private val TAG = this::class.java.simpleName

    // LiveData for holding network availability
    private val _networkAvailable = MutableLiveData(true)
    val networkAvailable: LiveData<Boolean> = _networkAvailable

    // StateFlow for holding logged-in user from Shared view model
    val savedUser: StateFlow<User> = sharedViewModel.savedUser

    // StateFlow for holding time-based recipe list state
    private val _timeBasedRecipeListState = MutableStateFlow<Result<List<Recipe>>>(Result.Loading)
    val timeBasedRecipeListState: StateFlow<Result<List<Recipe>>> = _timeBasedRecipeListState

    // StateFlow for holding calorie-based recipe list state
    private val _calorieBasedRecipeListState =
        MutableStateFlow<Result<List<Recipe>>>(Result.Loading)
    val calorieBasedRecipeListState: StateFlow<Result<List<Recipe>>> = _calorieBasedRecipeListState

    // MutableState for holding search keyword
    var searchKeyword by mutableStateOf("")

    init {
        /**
         * Initializes the network monitoring functionality for this ViewModel.
         *
         * This block creates a network callback to track network availability changes and
         * registers the network callback with the ConnectivityManager.
         */
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _networkAvailable.postValue(true)
            }

            override fun onLost(network: Network) {
                _networkAvailable.postValue(false)
            }
        }
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    /**
     * Fetches and updates recipes filtered by cooking time and calorie count asynchronously,
     * using the logged-in user's preferences for filtering.
     *
     * This method retrieves the logged-in user's preferred duration and calorie count from
     * the `sharedViewModel` and then calls the `filterRecipesByDuration` and
     * `filterRecipesByCalorie` methods to fetch recipes based on those preferences.
     */
    fun updateFilters(loggedUserId: Int, maxDuration: Int, maxCalorie: Int) {
        filterRecipesByDuration(
            loggedUserId = loggedUserId,
            maxDuration = maxDuration
        )
        filterRecipesByCalorie(
            loggedUserId = loggedUserId,
            maxCalorie = maxCalorie
        )
    }

    /**
     * Fetches recipes filtered by cooking time asynchronously.
     *
     * @param loggedUserId The ID of the logged-in user.
     * @param maxDuration The maximum cooking time of the recipes to filter.
     */
    fun filterRecipesByDuration(loggedUserId: Int, maxDuration: Int) {
        viewModelScope.launch {
            try {
                recipeManagementRepository.filterRecipesByDuration(loggedUserId, maxDuration)
                    .flowOn(Dispatchers.IO)
                    .collect { recipeListResult ->
                        _timeBasedRecipeListState.value = recipeListResult
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
                recipeManagementRepository.filterRecipesByCalorie(loggedUserId, maxCalorie)
                    .flowOn(Dispatchers.IO)
                    .collect { recipeListResult ->
                        _calorieBasedRecipeListState.value = recipeListResult
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
     * Checks the validity of the entered keyword.
     *
     * A valid keyword contains only letters (both uppercase and lowercase) and numbers,
     * and it is trimmed (i.e., no leading or trailing whitespace characters).
     *
     * @param enteredKeyword The keyword entered by the user to be validated.
     * @return `true` if the entered keyword is valid, `false` otherwise.
     */
    fun isValidKeyword(enteredKeyword: String): Boolean {
        // regular expression pattern that matches letters and numbers only
        val pattern = Regex("[a-zA-Z0-9]+")
        return enteredKeyword.trim().matches(pattern)
    }

    /**
     * Clear the search keyword.
     */
    fun clearSearchKeyword() {
        searchKeyword = ""
    }

    /**
     * Likes a recipe for the logged-in user.
     *
     * @param loggedUserId The ID of the logged-in user.
     * @param recipeId The ID of the recipe to like.
     * @param onSuccess Callback function to be invoked when the like operation is successful.
     * @param onFailure Callback function to be invoked when the like operation fails, with the error message as parameter.
     * @param onLoading Callback function to be invoked when the like operation is in progress.
     */
    fun likeRecipe(
        loggedUserId: Int,
        recipeId: Int,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
        onLoading: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                recipeManagementRepository.likeRecipe(loggedUserId, recipeId)
                    .flowOn(Dispatchers.IO)
                    .collect { likeResult ->
                        when (likeResult) {
                            is Result.Success -> onSuccess()
                            is Result.Failure -> onFailure(likeResult.error)
                            is Result.Loading -> onLoading()
                        }
                    }
            } catch (ex: Exception) {
                onFailure("An error occurred")
                Log.e(TAG, ex.message ?: "An error occurred", ex)
            }
        }
    }

    /**
     * Removes a like from a recipe for the logged-in user.
     *
     * @param loggedUserId The ID of the logged-in user.
     * @param recipeId The ID of the recipe to like.
     * @param onSuccess Callback function to be invoked when the remove like operation is successful.
     * @param onFailure Callback function to be invoked when the remove like operation fails, with the error message as parameter.
     * @param onLoading Callback function to be invoked when the remove like operation is in progress.
     */
    fun removeLikeRecipe(
        loggedUserId: Int,
        recipeId: Int,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
        onLoading: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                recipeManagementRepository.removeLikeRecipe(loggedUserId, recipeId)
                    .flowOn(Dispatchers.IO)
                    .collect { dislikeResult ->
                        when (dislikeResult) {
                            is Result.Success -> onSuccess()
                            is Result.Failure -> onFailure(dislikeResult.error)
                            is Result.Loading -> onLoading()
                        }
                    }
            } catch (ex: Exception) {
                onFailure("An error occurred")
                Log.e(TAG, ex.message ?: "An error occurred", ex)
            }
        }
    }

    /**
     * Adds a recipe to my favorites.
     *
     * @param loggedUserId The ID of the logged-in user.
     * @param recipeId The ID of the recipe to add to my favorites.
     * @param onSuccess Callback function to be invoked when add favorite operation is successful.
     * @param onFailure Callback function to be invoked when add favorite operation fails, with the error message as parameter.
     * @param onLoading Callback function to be invoked when add favorite operation is in progress.
     */
    fun addFavoriteRecipe(
        loggedUserId: Int,
        recipeId: Int,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
        onLoading: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                recipeManagementRepository.addFavoriteRecipe(loggedUserId, recipeId)
                    .flowOn(Dispatchers.IO)
                    .collect { favoriteResult ->
                        when (favoriteResult) {
                            is Result.Success -> onSuccess()
                            is Result.Failure -> onFailure(favoriteResult.error)
                            is Result.Loading -> onLoading()
                        }
                    }
            } catch (ex: Exception) {
                onFailure("An error occurred")
                Log.e(TAG, ex.message ?: "An error occurred", ex)
            }
        }
    }

    /**
     * Removes a recipe from my favorites.
     *
     * @param loggedUserId The ID of the logged-in user.
     * @param recipeId The ID of the recipe to remove from my favorites.
     * @param onSuccess Callback function to be invoked when remove favorite operation is successful.
     * @param onFailure Callback function to be invoked when remove favorite operation fails, with the error message as parameter.
     * @param onLoading Callback function to be invoked when remove favorite operation is in progress.
     */
    fun removeFavoriteRecipe(
        loggedUserId: Int,
        recipeId: Int,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
        onLoading: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                recipeManagementRepository.removeFavoriteRecipe(loggedUserId, recipeId)
                    .flowOn(Dispatchers.IO)
                    .collect { removeFavoriteResult ->
                        when (removeFavoriteResult) {
                            is Result.Success -> onSuccess()
                            is Result.Failure -> onFailure(removeFavoriteResult.error)
                            is Result.Loading -> onLoading()
                        }
                    }
            } catch (ex: Exception) {
                onFailure("An error occurred")
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