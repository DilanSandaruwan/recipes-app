package com.gtp01.group01.android.recipesmobileapp.shared.sources.Local

import android.content.SharedPreferences
import javax.inject.Inject

class LocalDataSource@Inject constructor(private val sharePreferences: SharedPreferences) {

    fun saveUserId(userId: Int) {
        sharePreferences.edit().putInt("USERID", userId).apply()
    }
    fun deleteUserId() {
        sharePreferences.edit().remove("USERID").apply()
    }
}