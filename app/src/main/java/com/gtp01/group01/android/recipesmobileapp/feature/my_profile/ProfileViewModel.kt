package com.gtp01.group01.android.recipesmobileapp.feature.my_profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.gtp01.group01.android.recipesmobileapp.shared.model.AuthUser
import com.gtp01.group01.android.recipesmobileapp.feature.my_profile.repository.AuthRepository
import com.gtp01.group01.android.recipesmobileapp.feature.my_profile.repository.GetUserIdRepository
import com.gtp01.group01.android.recipesmobileapp.shared.common.ResultState
import com.gtp01.group01.android.recipesmobileapp.shared.sources.Local.LocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getUserIdRepository: GetUserIdRepository,
    private val localDataSource: LocalDataSource // Inject LocalDataSource
) : ViewModel() {

    private val _saveUserResult = MutableLiveData<Boolean>()

    private val _userId = MutableLiveData<ResultState>()


}
