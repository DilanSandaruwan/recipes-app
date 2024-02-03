package com.gtp01.group01.android.recipesmobileapp.sources

import com.gtp01.group01.android.recipesmobileapp.constant.ConstantNetworkService
import com.gtp01.group01.android.recipesmobileapp.data.AuthUser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query


interface AuthApiService {

    @Headers("Accept: application/json; utf-8")
    @POST(ConstantNetworkService.REPOSITORIES_ENDPOINT)
    suspend fun saveUser(@Body authUser: AuthUser)
    : Response<AuthUser>

}