package com.gtp01.group01.android.recipesmobileapp.feature.my_recipes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

@HiltViewModel
class MyRecipesViewModel @Inject constructor(
    private val recipeManagementRepository: RecipeManagementRepository
) : ViewModel() {

    // LiveData for holding logged in user
    private val _user = MutableLiveData<User?>(null)
    val user: LiveData<User?> = _user

    // LiveData for
    private val _myRecipesList = MutableStateFlow<Result<List<Recipe>>>(Result.Loading)
    val myRecipesList: StateFlow<Result<List<Recipe>>> = _myRecipesList

    /**
     *
     */
    fun getMyRecipes(idLoggedUser: Int) {
        viewModelScope.launch {
            try {
                recipeManagementRepository.getMyRecipes(idLoggedUser)
                    .flowOn(Dispatchers.IO)
                    .collect { recipeList ->
                        _myRecipesList.value = recipeList
                    }
            } catch (ex: Exception) {
                _myRecipesList.value = Result.Failure("Exception")
                Log.e("MyRecipesViewModel", ex.message ?: "An error occurred", ex)
            }
        }
    }
}
