package com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.FavouriteViewModel
import com.gtp01.group01.android.recipesmobileapp.feature.my_favourites.repository.Repository

class FavoriteViewModelFactory(val app: Application, val Repository: Repository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavouriteViewModel(app, Repository)as T
    }


}