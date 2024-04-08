package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val _favoriteRecipes = MutableLiveData<List<Recipe>>()
    val favoriteRecipes: LiveData<List<Recipe>> get() = _favoriteRecipes


    fun fetchFavoriteRecipes(userId: Int) {
        viewModelScope.launch {
            val recipes = favoriteRepository.getFavoriteRecipesByUserId(userId)
            _favoriteRecipes.value = recipes
            }
        }



}
