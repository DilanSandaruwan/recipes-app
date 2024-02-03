package com.gtp01.group01.android.recipesmobileapp.sources

import com.google.android.gms.common.api.Api
import com.gtp01.group01.android.recipesmobileapp.data.AuthUser
import retrofit2.Response
import retrofit2.http.Body
import javax.inject.Inject

class ApiImpl @Inject constructor(private val api : AuthApiService) {

    suspend fun saveUser(authUser: AuthUser)= api.saveUser(authUser)
    }
