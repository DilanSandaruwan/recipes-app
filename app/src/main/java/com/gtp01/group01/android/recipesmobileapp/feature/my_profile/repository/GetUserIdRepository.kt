package com.gtp01.group01.android.recipesmobileapp.feature.my_profile.repository

import com.gtp01.group01.android.recipesmobileapp.shared.model.UserIdResponse
import com.gtp01.group01.android.recipesmobileapp.shared.sources.AuthApiService
import com.gtp01.group01.android.recipesmobileapp.shared.sources.UserIdApiService
import javax.inject.Inject

class GetUserIdRepository @Inject constructor(private val userIdApiService: UserIdApiService){

    suspend fun getUserId(userEmail: String): UserIdResponse {
        return userIdApiService.getUserId(userEmail)
    }
}