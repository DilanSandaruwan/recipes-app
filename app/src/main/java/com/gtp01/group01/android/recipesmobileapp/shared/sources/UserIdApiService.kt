package com.gtp01.group01.android.recipesmobileapp.shared.sources

import com.gtp01.group01.android.recipesmobileapp.constant.ConstantNetworkService
import com.gtp01.group01.android.recipesmobileapp.shared.model.UserIdResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
/**
 * Retrofit service interface for fetching user ID based on the provided email.
 */
interface UserIdApiService {
    /**
     * Retrieves the user ID associated with the provided email address.
     *
     * @param userEmail The email address of the user whose ID is to be retrieved.
     * @return A [UserIdResponse] containing the user ID.
     */
    @Headers(ConstantNetworkService.ACCEPT_JSON_UTF8)
    @GET(ConstantNetworkService.SEARCH_USER_BY_EMAIL_ENDPOINT)
    suspend fun getUserId(@Path("useremail") userEmail: String): UserIdResponse

}