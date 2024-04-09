package com.gtp01.group01.android.recipesmobileapp.feature.home.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantResponseCode
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
 * ViewModel responsible for managing search results and network connectivity in the search feature.
 *
 * This ViewModel facilitates searching for recipes based on the recipe name entered by the user.
 * It also monitors network connectivity to ensure proper handling of network-related operations.
 *
 * @param connectivityManager The system service for managing network connectivity.
 * @param recipeManagementRepository The repository for fetching recipe data.
 * @property sharedViewModel The shared view model holding logged-in user information.
 */
@HiltViewModel
class SearchResultViewModel @Inject constructor(
    connectivityManager: ConnectivityManager,
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

    // StateFlow for holding searched recipe list state
    private val _searchResultRecipeListState =
        MutableStateFlow<Result<List<Recipe>>>(Result.Loading)
    val searchResultRecipeListState: StateFlow<Result<List<Recipe>>> = _searchResultRecipeListState

    /**
     * Initializes the network monitoring functionality for this ViewModel.
     *
     * This block creates a network callback to track network availability changes and
     * registers the network callback with the ConnectivityManager.
     */
    init {
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
     * Fetches recipes filtered by recipe category asynchronously.
     *
     * @param loggedUserId The ID of the logged-in user.
     * @param categoryId The category of the recipes to filter.
     */
    fun filterRecipesByCategory(loggedUserId: Int, categoryId: Int) {
        viewModelScope.launch {
            try {
                _searchResultRecipeListState.value = Result.Loading
                recipeManagementRepository.filterRecipesByCategory(loggedUserId, categoryId)
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