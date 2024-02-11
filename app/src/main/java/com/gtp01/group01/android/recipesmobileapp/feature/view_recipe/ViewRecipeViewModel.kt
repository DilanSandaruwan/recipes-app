package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtp01.group01.android.recipesmobileapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewRecipeViewModel @Inject constructor(private val viewRecipeRepository: ViewRecipeRepository): ViewModel() {
    fun fetchRecipeDetail(idLoggedUser: Int, recipeName: String) {
        viewModelScope.launch {
            val recipe = viewRecipeRepository.getRecipeDetail(idLoggedUser, recipeName)
            // Handle the recipe data, update UI, etc.
        }
    }
}