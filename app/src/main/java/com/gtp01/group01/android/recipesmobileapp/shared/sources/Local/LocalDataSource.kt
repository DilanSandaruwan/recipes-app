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
     * commit() guarantees synchronous data persistence, meaning it waits for the data to be written
     * to disk before returning. This ensures the user ID is saved before the navigation
     * to the next screen. A slight UI freeze is acceptable.
     *
     * @param userId The user ID to be saved.
     */
    fun saveUserId(userId: Int) {

        sharePreferences.edit().putInt("USERID", userId).commit()

    }
    /**
     * Retrieves the user ID from SharedPreferences. Return 0 by default.
     *
     * @return USERID The user ID of logged in user.
     */
    fun getUserId(): Int {
        return sharePreferences.getInt("USERID", 0)

    }
    /**
     * Deletes the user ID from SharedPreferences.
     * commit() guarantees synchronous data persistence, meaning it waits for the data to be deleted
     * from the disk before returning. This ensures the user ID is deleted before the navigation
     * to the next screen. A slight UI freeze is acceptable.
     */
    fun deleteUserId() {

        sharePreferences.edit().remove("USERID").commit()

    }

}