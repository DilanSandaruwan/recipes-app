package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.ViewRecipeRepository
import com.gtp01.group01.android.recipesmobileapp.shared.common.Result
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for providing recipe details to the associated UI components.
 * @param viewRecipeRepository The repository for fetching recipe details.
 */
@HiltViewModel
class ViewRecipeViewModel @Inject constructor(
    private val viewRecipeRepository: ViewRecipeRepository
) : ViewModel() {
    /** LiveData to observe the result of fetching recipe details. */
    val recipeDetails = MutableLiveData<Result>()
    /** Private LiveData to hold the actual recipe details. */
    private val _recipeDetails = MutableLiveData<Recipe?>()

    init {
        // Initialize recipeDetails with the complete data
        _recipeDetails.value = null
    }

    /**
     * Fetches recipe details from the repository and updates the [recipeDetails] LiveData accordingly.
     * @param idLoggedUser The ID of the logged-in user.
     * @param idrecipe The ID of the recipe to fetch details for.
     */
    fun fetchRecipeDetail(idLoggedUser: Int, idrecipe: Int) {

        viewModelScope.launch {
            try {
                // Post loading state
                recipeDetails.postValue(Result.Loading)
                // Fetch recipe details from the repository
                val result = viewRecipeRepository.getRecipeDetail(idLoggedUser, idrecipe)

                // Update the recipeDetails LiveData with the successful result
                recipeDetails.postValue(Result.Success(result))
            } catch (e: Exception) {
                // Update the recipeDetails LiveData with the error result if an exception occurs
                recipeDetails.postValue(Result.Failure(e.message ?: "Unknown error occurred"))
            }
        }
    }


}
