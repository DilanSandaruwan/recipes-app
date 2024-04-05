package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.models.Recipes
import com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.repository.Repository
import kotlinx.coroutines.launch

class FavouriteViewModel(app: Application, val Repository: Repository ): AndroidViewModel(app) {

    fun addToFavorites(recipes: Recipes) = viewModelScope.launch {
        Repository.upsert(recipes)
    }
    fun getFavoriteRecipes() = Repository.getFavorite()
    fun deleteRecipe (recipes: Recipes) = viewModelScope.launch {
        Repository.deleteRecipe(recipes)
    }
}