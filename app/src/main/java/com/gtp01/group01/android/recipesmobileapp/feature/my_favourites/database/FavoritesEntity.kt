package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.util.Constant.Companion.FAVORITE_RECIPES_TABLE


@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

)