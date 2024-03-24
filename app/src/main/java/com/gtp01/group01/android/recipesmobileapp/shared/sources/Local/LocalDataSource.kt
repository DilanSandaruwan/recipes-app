package com.gtp01.group01.android.recipesmobileapp.shared.sources.Local

import android.content.SharedPreferences
import javax.inject.Inject

/**
 * A data source class responsible for saving and deleting user-related data in SharedPreferences.
 *
 * @property sharedPreferences The SharedPreferences instance used for data storage.
 */
class LocalDataSource @Inject constructor(private val sharePreferences: SharedPreferences) {
    /**
     * Saves the provided user ID into SharedPreferences.
     *
     * @param userId The user ID to be saved.
     */
    fun saveUserId(userId: Int) {
        sharePreferences.edit().putInt("USERID", userId).apply()
    }

    /**
     * Deletes the user ID from SharedPreferences.
     */
    fun deleteUserId() {
        sharePreferences.edit().remove("USERID").apply()
    }
}