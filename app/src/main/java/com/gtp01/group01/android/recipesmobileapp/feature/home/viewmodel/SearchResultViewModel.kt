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
 */
@HiltViewModel
class SearchResultViewModel @Inject constructor(
    connectivityManager: ConnectivityManager,
    private val recipeManagementRepository: RecipeManagementRepository
) : ViewModel() {
    // Logging tag for this class
    private val TAG = this::class.java.simpleName

    // LiveData for holding network availability
    private val _networkAvailable = MutableLiveData(true)
    val networkAvailable: LiveData<Boolean> = _networkAvailable

    // LiveData for holding logged-in user
    private val _user = MutableLiveData<User?>(null)
    val user: LiveData<User?> = _user

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
        val networkRequest = NetworkRequest.Builder().build()
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