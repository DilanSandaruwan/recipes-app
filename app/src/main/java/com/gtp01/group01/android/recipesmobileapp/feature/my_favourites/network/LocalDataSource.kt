package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.network

import com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.database.FavoritesEntity
import com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.database.RecipesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


    class LocalDataSource @Inject constructor(
        private val recipesDao: RecipesDao
    )
    {

    fun readFavoriteRecipes(): Flow<List<FavoritesEntity>> {
        return recipesDao.readFavoriteRecipes()
    }
suspend fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity) {
    recipesDao.insertFavoriteRecipe(favoritesEntity)
}

suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity) {
    recipesDao.deleteFavoriteRecipe(favoritesEntity)
}

suspend fun deleteAllFavoriteRecipes() {
    recipesDao.deleteAllFavoriteRecipes()
}



}