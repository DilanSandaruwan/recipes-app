package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.repository

import com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.database.RecipesDatabase
import com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.models.Recipes
import com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.network.LocalDataSource
import javax.inject.Inject

class Repository(val database: RecipesDatabase) {




    suspend fun upsert(recipes: Recipes) = database.recipesDao().upsert(recipes)
    fun getFavorite() = database.recipesDao().getAllRecipes()

    suspend fun deleteRecipe(recipes: Recipes) = database.recipesDao().deleteRecipes(recipes)
}