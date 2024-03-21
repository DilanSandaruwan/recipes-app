package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [ FavoritesEntity::class],
    version = 1,
    exportSchema = false
)
//@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase: RoomDatabase() {

    abstract fun recipesDao(): RecipesDao

    companion object{
        @Volatile
        private var instance: RecipesDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RecipesDatabase::class.java,
                "Recipes_database.database"
            ).build()


    }

}