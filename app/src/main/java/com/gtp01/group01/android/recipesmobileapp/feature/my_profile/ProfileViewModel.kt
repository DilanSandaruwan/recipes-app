package com.gtp01.group01.android.recipesmobileapp.feature.my_profile

import androidx.lifecycle.ViewModel
import com.gtp01.group01.android.recipesmobileapp.repository.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel  @Inject constructor(
    private val repository: AuthRepositoryImpl,
): ViewModel(){


    fun logout() {
        repository.signOut()
    }

}