package com.gtp01.group01.android.recipesmobileapp.feature.home.viewmodel

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
}