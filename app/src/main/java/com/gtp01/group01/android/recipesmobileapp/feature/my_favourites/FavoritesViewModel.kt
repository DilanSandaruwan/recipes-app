package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtp01.group01.android.recipesmobileapp.shared.common.ResultState
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val _favoriteRecipes = MutableLiveData<List<Recipe>>()
    val favoriteRecipes: LiveData<List<Recipe>> get() = _favoriteRecipes

    fun fetchFavoriteRecipes() {
        viewModelScope.launch {
            _favoriteRecipes.value = favoriteRepository.getFavoriteRecipes().value
        }
    }

    fun addToFavorites(userId: Int, recipeId: Int) {
        viewModelScope.launch {
            try {
                favoriteRepository.addFavorite(userId, recipeId)
                fetchFavoriteRecipes()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun removeFromFavorites(userId: Int, recipeId: Int) {
        viewModelScope.launch {
            try {
                favoriteRepository.removeFavorite(userId, recipeId)
                fetchFavoriteRecipes()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
