package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe

import com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.remote.RemoteDataSource
import javax.inject.Inject

class ViewRecipeRepo @Inject constructor(
    private val remoteDataSource: RemoteDataSource
){


    fun getLoggedUserid(userid:Int) {

        // Implement logic to fetch data for the logged user id
        remoteDataSource.getLoggedUserid(userid)

    }
    fun getRecipeName(recipeName:String) {

        remoteDataSource.getRecipeName(recipeName)

    }

}