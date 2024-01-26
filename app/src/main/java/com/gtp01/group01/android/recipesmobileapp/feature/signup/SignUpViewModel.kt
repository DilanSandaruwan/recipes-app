package com.gtp01.group01.android.recipesmobileapp.feature.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gtp01.group01.android.recipesmobileapp.constant.UIState
import com.gtp01.group01.android.recipesmobileapp.repository.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel  @Inject constructor(
    private val repository: AuthRepositoryImpl,
) : ViewModel() {
    //current sign-up status
    private val _signUp = MutableLiveData<UIState>()
    val signUp: LiveData<UIState>
        get() = _signUp

    /**
     * Initiates the sign-up process with the provided email and password.
     *
     * @param email The email address used for sign-up.
     * @param password The password associated with the account being created.
     */
    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            // Set the initial loading state
            _signUp.postValue(UIState.Loading)
            // Perform the login operation
            repository.signUp(email, password).addOnSuccessListener {
                _signUp.postValue(UIState.Success(it.user))
            }.addOnFailureListener {
                // Login failed
                _signUp.postValue(UIState.Failure("Sign up failed"))
            }

        }
    }

}