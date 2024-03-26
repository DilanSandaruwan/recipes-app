package com.gtp01.group01.android.recipesmobileapp.feature.home.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtp01.group01.android.recipesmobileapp.feature.my_profile.repository.RecipeManagementRepository
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import com.gtp01.group01.android.recipesmobileapp.shared.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
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

    // LiveData for holding logged in user
    private val _user = MutableLiveData<User?>(null)
    val user: LiveData<User?> = _user

    // LiveData for holding time-based recipe list
    private val _timeBasedRecipeList = MutableLiveData<List<Recipe>>(emptyList())
    val timeBasedRecipeList: LiveData<List<Recipe>> = _timeBasedRecipeList

    // LiveData for holding calorie-based recipe list
    private val _calorieBasedRecipeList = MutableLiveData<List<Recipe>>(emptyList())
    val calorieBasedRecipeList: LiveData<List<Recipe>> = _calorieBasedRecipeList

    // The current search keyword entered by the user
    var searchKeyword by mutableStateOf("")

    /**
     * Filters recipes based on duration.
     *
     * @param idLoggedUser The ID of the logged-in user.
     * @param maxDuration The maximum duration for filtering recipes.
     */
    fun filterRecipesByDuration(idLoggedUser: Int, maxduration: Int) {
        viewModelScope.launch {
            _timeBasedRecipeList.value =
                recipeManagementRepository.filterRecipesByDuration(idLoggedUser, maxduration)
        }
    }

    /**
     * Filters recipes based on calorie.
     *
     * @param idLoggedUser The ID of the logged-in user.
     * @param maxCalorie The maximum calorie count for filtering recipes.
     */
    fun filterRecipesByCalorie(idLoggedUser: Int, maxCalorie: Int) {
        viewModelScope.launch {
            _calorieBasedRecipeList.value =
                recipeManagementRepository.filterRecipesByDuration(idLoggedUser, maxCalorie)
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
        val decodedBytes: ByteArray =
            android.util.Base64.decode(imageValue, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}