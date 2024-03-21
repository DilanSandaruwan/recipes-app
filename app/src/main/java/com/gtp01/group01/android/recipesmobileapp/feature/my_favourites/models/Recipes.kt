package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "recipes"
)

data class Recipes(
             @PrimaryKey(autoGenerate = true)
             var id: Int? = null

)
