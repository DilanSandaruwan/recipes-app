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
    val saveUserResult: LiveData<Boolean> = _saveUserResult
    val userId: LiveData<ResultState> = _userId

    /**
     * Save the user details to the backend using the [authRepository].
     * Retrieves the current authenticated user's details from Firebase Authentication
     * and creates an [AuthUser] object to store in the backend.
     */
    fun saveUser() {
        viewModelScope.launch {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                // Create an AuthUser object with the retrieved details
                val authUser = AuthUser(
                    idUser = 1, // You might get this ID from Firebase or another source
                    email = currentUser.email.orEmpty(),
                    fullName = currentUser.displayName.orEmpty()
                )

                // Save the user details using the AuthRepository
                val response = authRepository.saveUser(authUser)
                _saveUserResult.value = response.isSuccessful
            } else {
                // Handle the case where the user is not authenticated
                _saveUserResult.value = false
            }
        }
    }

    /**
     * Fetches the user ID based on the provided email.
     */

    fun getUserId() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        viewModelScope.launch {

            try {
                val email = currentUser?.email.orEmpty()
                val result = getUserIdRepository.getUserId(email)
                _userId.value = ResultState.Success(result)
                localDataSource.saveUserId(result.iduser)

            } catch (e: Exception) {
                _userId.value = ResultState.Failure("Failed to fetch user ID")
            }
        }
    }

}
