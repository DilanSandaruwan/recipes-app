package com.gtp01.group01.android.recipesmobileapp.repository

import android.util.Log

import com.gtp01.group01.android.recipesmobileapp.constant.ReceipeResponse
import com.gtp01.group01.android.recipesmobileapp.data.AuthUser
import com.gtp01.group01.android.recipesmobileapp.sources.ApiImpl
import com.gtp01.group01.android.recipesmobileapp.sources.AuthApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

import javax.inject.Inject

class AuthRepository @Inject constructor(private val authApiService: AuthApiService) {

    suspend fun saveUser(authUser: AuthUser): Response<AuthUser> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("AuthRepository", "Attempting to save user: $authUser")
                val response = authApiService.saveUser(authUser)

                // Log the entire response (you might want to log specific details based on your needs)
                Log.d("AuthRepository", "Save user response: $response")

                // Check if the response is successful based on the HTTP status code
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.d("AuthRepository", "User saved successfully. Response: $responseBody")
                        // You can access individual properties of the ReceipeResponse if needed

                    } else {
                        Log.e("AuthRepository", "Response body is null")
                    }
                } else {
                    // Log the error message from the response body or other relevant details
                    Log.e("AuthRepository", "Failed to save user. Response: ${response.errorBody()?.string()}")
                }

                // Return the response to match the declared return type
                response
            } catch (e: Exception) {
                Log.e("AuthRepository", "Error while saving user: ${e.message}", e)
                throw e  // Rethrow the exception after logging
            }
        }
    }
}