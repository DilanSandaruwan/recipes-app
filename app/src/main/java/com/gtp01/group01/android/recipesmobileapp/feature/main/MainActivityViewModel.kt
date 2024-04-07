package com.gtp01.group01.android.recipesmobileapp.feature.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.gtp01.group01.android.recipesmobileapp.constant.UserDefaultConstant.GUEST_USER_ID
import com.gtp01.group01.android.recipesmobileapp.feature.my_profile.repository.AuthRepository
import com.gtp01.group01.android.recipesmobileapp.feature.my_profile.repository.GetUserIdRepository
import com.gtp01.group01.android.recipesmobileapp.shared.common.ResultState
import com.gtp01.group01.android.recipesmobileapp.shared.common.viewmodel.SharedViewModel
import com.gtp01.group01.android.recipesmobileapp.shared.model.User
import com.gtp01.group01.android.recipesmobileapp.shared.sources.Local.LocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getUserIdRepository: GetUserIdRepository,
    private val sharedViewModel: SharedViewModel, // Shared ViewModel used for sharing data across different screens
    private val localDataSource: LocalDataSource // Inject LocalDataSource
) : ViewModel() {
    private val _saveUserResult = MutableLiveData<Boolean>()

    private val _userId = MutableLiveData<ResultState>()

    /**
     * Save the user details to the backend using the [authRepository].
     * Retrieves the current authenticated user's details from Firebase Authentication
     * and creates an [User] object to store in the backend.
     * Saved logged in user's id in local storage and User object in Shared view model.
     */
    fun saveUser() {
        viewModelScope.launch {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                val authUser = User(
                    idUser = 1, // You might get this ID from Firebase or another source
                    email = currentUser.email.orEmpty(),
                    fullName = currentUser.displayName.orEmpty()
                )

                // Save user details using the AuthRepository
                val response = try {
                    authRepository.saveUser(authUser)
                } catch (e: Exception) {
                    // Handle potential exceptions during user saving (network issues, etc.)
                    _saveUserResult.value = false

                    // Save as a guest user in Shared preferences and Shared view model if error occurred during user saving
                    saveUserInLocalStorage(User(idUser = GUEST_USER_ID))
                    return@launch // Exit coroutine on exception
                }
                // Check for successful response
                if (response.isSuccessful) {
                    val savedUser = response.body()
                    if (savedUser != null) {
                        // Save new user in local storage and shared view model
                        saveUserInLocalStorage(savedUser)
                        _saveUserResult.value = true
                    } else {
                        // Save as a guest user in Shared preferences and Shared view model if response is null
                        saveUserInLocalStorage(User(idUser = GUEST_USER_ID))
                        _saveUserResult.value = false
                    }
                } else {
                    // Handle unsuccessful save operation (network error, server error, etc.)
                    _saveUserResult.value = false

                    // Retrieve user from backend and store if logged in user has already registered
                    getUserId()
                }
            } else {
                // Handle the case where the user is not authenticated
                _saveUserResult.value = false

                // Save as a guest user in Shared preferences and Shared view model if user is not authenticated
                saveUserInLocalStorage(User(idUser = GUEST_USER_ID))
            }
        }
    }

    /**
     * Fetches the user ID based on the provided email.
     */
    private fun getUserId() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        viewModelScope.launch {
            try {
                val email = currentUser?.email.orEmpty()
                val result = getUserIdRepository.getUserId(email)
                _userId.value = ResultState.Success(result)

                // Save user id of existing user in Shared preferences and Shared view model
                saveUserInLocalStorage(result)
            } catch (e: Exception) {
                _userId.value = ResultState.Failure("Failed to fetch user ID")

                // Save as a guest user in Shared preferences and Shared view model if error occurred during data retrieval
                saveUserInLocalStorage(User(idUser = GUEST_USER_ID))
            }
        }
    }

    /**
     * Stores the provided user in the shared ViewModel and user id in local storage
     *
     * @param currentUser The user to store
     */
    private fun saveUserInLocalStorage(currentUser: User) {
        // Saves the user ID in a local data storage
        localDataSource.saveUserId(currentUser.idUser)

        // Updates the user in the shared ViewModel to make it accessible for other screens of the app
        sharedViewModel.setCurrentUser(currentUser)
    }
}