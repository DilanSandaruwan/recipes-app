package com.gtp01.group01.android.recipesmobileapp.feature.recipe_add_update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtp01.group01.android.recipesmobileapp.repository.RecipeManagementRepository
import com.gtp01.group01.android.recipesmobileapp.shared.models.NutritionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for adding or updating a recipe.
 *
 * @property recipeManagementRepository The repository for managing recipe data.
 * @property _nutritionList Internal mutable live data for nutrition information.
 * @property nutrition Live data for observing nutrition information changes.
 */
@HiltViewModel
class RecipeAddUpdateViewModel @Inject constructor(
    private val recipeManagementRepository: RecipeManagementRepository
) : ViewModel() {
    private val _nutritionList = MutableLiveData<List<NutritionModel>>()
    val nutrition: LiveData<List<NutritionModel>> = _nutritionList

    /**
     * Fetches nutrition information based on the provided ingredients.
     *
     * @param ingredients The ingredients to fetch nutrition information for.
     */
    fun getNutritionsVm(ingredients: String) {
        viewModelScope.launch {
            val response = recipeManagementRepository.getNutritionsRepo(ingredients)
            _nutritionList.postValue(response ?: emptyList())
        }
    }
}