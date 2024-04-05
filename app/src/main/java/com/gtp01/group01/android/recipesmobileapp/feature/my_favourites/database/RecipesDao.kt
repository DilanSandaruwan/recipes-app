package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.models.Recipes
import kotlinx.coroutines.flow.Flow
@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(recipes: Recipes): Long

    @Query("SELECT * FROM recipes")
    fun getAllRecipes():LiveData<List<Recipes>>
    @Delete
    suspend fun deleteRecipes(recipes: Recipes)



}