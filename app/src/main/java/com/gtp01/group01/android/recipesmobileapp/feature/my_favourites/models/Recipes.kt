package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(
    tableName = "recipes"
)

data class Recipes(
             @PrimaryKey(autoGenerate = true)
             var id: Int? = null,
             var title: String,
             var description:String

):Serializable
