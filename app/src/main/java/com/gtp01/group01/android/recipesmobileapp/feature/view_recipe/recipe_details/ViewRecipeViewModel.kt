package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.recipe_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtp01.group01.android.recipesmobileapp.shared.common.Result
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.ViewRecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewRecipeViewModel  @Inject constructor(private val viewRecipeRepository: ViewRecipeRepository
): ViewModel() {

    val recipeDetails = MutableLiveData<Result>()
private val _recipeDetails = MutableLiveData<Recipe?>()
    init {
        // Initialize recipeDetails with the complete data
        _recipeDetails.value = null
    }


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
