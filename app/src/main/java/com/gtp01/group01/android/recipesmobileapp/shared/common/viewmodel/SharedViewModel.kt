package com.gtp01.group01.android.recipesmobileapp.shared.common.viewmodel

import androidx.lifecycle.ViewModel
import com.gtp01.group01.android.recipesmobileapp.constant.UserDefaultConstant
import com.gtp01.group01.android.recipesmobileapp.constant.UserDefaultConstant.GUEST_USER_ID
import com.gtp01.group01.android.recipesmobileapp.shared.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Shared ViewModel class responsible for managing and sharing the currently logged-in user's information
 * across different parts of the application. This ViewModel utilizes StateFlow for reactive updates
 * to the user information.
 */
class SharedViewModel : ViewModel() {
    /**
     * Holds the current user's information as a StateFlow,
     * providing a reactive stream for updates to this information.
     *
     * This StateFlow is used for sharing the user information
     * across different view models and components within the application.
     */
    private val _savedUser = MutableStateFlow(User(idUser = GUEST_USER_ID))
    val savedUser: StateFlow<User> = _savedUser.asStateFlow()

    /**
     * Validates and sets the information for the currently logged-in user.
     *
     * - Checks the validity of the provided user information,
     *   ensuring proper handling of guest user scenarios.
     * - Sets default preference values for users if necessary.
     * - Updates the `savedUser` StateFlow with the validated user data.
     *
     * @param user The user object containing the information to be validated and set.
     */
    fun setCurrentUser(user: User) {
        // If user id is 0, uses default values for all preferences.
        if (user.idUser == GUEST_USER_ID) {
            _savedUser.value = User(
                idUser = GUEST_USER_ID,
                preferDuration = UserDefaultConstant.GUEST_DURATION_PREFERENCE,
                preferCalorie = UserDefaultConstant.GUEST_CALORIE_PREFERENCE
            )
        } else {
            // Handles the case where valid user data is available
            _savedUser.value = User(
                idUser = user.idUser,
                email = user.email,
                fullName = user.fullName,
                preferCategories = user.preferCategories,

                // Assigns user's preferred duration, using a default of 30 if it's null or 0
                preferDuration = user.preferDuration.takeIf { it > 0 }
                    ?: UserDefaultConstant.GUEST_DURATION_PREFERENCE,

                // Assigns user's preferred calorie, using a default of 300 if it's null or 0
                preferCalorie = user.preferCalorie.takeIf { it > 0 }
                    ?: UserDefaultConstant.GUEST_CALORIE_PREFERENCE,
            )
        }
    }
}