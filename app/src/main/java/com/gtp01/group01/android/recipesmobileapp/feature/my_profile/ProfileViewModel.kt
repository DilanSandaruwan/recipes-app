package com.gtp01.group01.android.recipesmobileapp.feature.my_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.gtp01.group01.android.recipesmobileapp.data.AuthUser
import com.gtp01.group01.android.recipesmobileapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel  @Inject constructor(private  val authRepository: AuthRepository
): ViewModel(){

    private val _saveUserResult = MutableLiveData<Boolean>()

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
                    iduser = 1, // You might get this ID from Firebase or another source
                    email = currentUser.email.orEmpty(),
                    fullname = currentUser.displayName.orEmpty()
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
}
