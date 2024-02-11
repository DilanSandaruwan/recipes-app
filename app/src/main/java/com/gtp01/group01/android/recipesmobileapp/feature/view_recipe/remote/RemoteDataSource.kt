package com.gtp01.group01.android.recipesmobileapp.feature.view_recipe.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val dataSource: RemoteDataSource){

    fun getLoggedUserid(userid: Int) {
        // Implement logic to fetch user data based on userid
        println("Fetching user data for userid: $userid")
    }

    fun getRecipeName(recipeName: String) {
        // Implement logic to fetch recipe data based on recipeName
        println("Fetching recipe data for recipeName: $recipeName")
    }

}