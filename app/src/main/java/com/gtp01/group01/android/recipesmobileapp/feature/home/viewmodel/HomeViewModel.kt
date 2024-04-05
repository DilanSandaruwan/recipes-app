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
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import com.gtp01.group01.android.recipesmobileapp.shared.model.User
import com.gtp01.group01.android.recipesmobileapp.shared.sources.Local.LocalDataSource
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
 * such as cooking time and calorie count. It also handles network connectivity and user input for searching.
 *
 * @property recipeManagementRepository The repository for managing recipe data.
 * @param recipeManagementRepository The repository for fetching recipe data.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    connectivityManager: ConnectivityManager,
    private val recipeManagementRepository: RecipeManagementRepository,
    private val localDataSource: LocalDataSource
) : ViewModel() {
    // Logging tag for this class
    private val TAG = this::class.java.simpleName

    // LiveData for holding network availability
    private val _networkAvailable = MutableLiveData(true)
    val networkAvailable: LiveData<Boolean> = _networkAvailable

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
        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
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
     * Fetches and sets the currently logged-in user's information.
     *
     * This function retrieves the user ID from the local data source using `localDataSource.getUserId()`.
     * It then creates a new `User` object with the retrieved ID and sets the value of the private member variable `_user`
     * to this newly created user object.
     */
    fun getUser() {
        _user.value = User(
            idUser = localDataSource.getUserId()
        )
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