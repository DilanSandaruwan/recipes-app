package com.gtp01.group01.android.recipesmobileapp.shared.sources

import com.gtp01.group01.android.recipesmobileapp.constant.ConstantNetworkService
import com.gtp01.group01.android.recipesmobileapp.shared.model.AuthUser
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interface representing the authentication API service.
 */
interface AuthApiService {
    /**
     * Save user details to the backend.
     *
     * @param authUser The [AuthUser] object containing user details.
     * @return A [Response] indicating the success or failure of the save operation.
     */
    @Headers("Accept: application/json; utf-8")
    @POST(ConstantNetworkService.AUTH_DETAIL_USER_ENDPOINT)
    suspend fun saveUser(@Body authUser: AuthUser)
    : Response<AuthUser>
    @Headers("Accept: application/json; utf-8")
    @GET(ConstantNetworkService.RECIPE_DETAIL_BY_RECIPE_NAME_ENDPOINT)
    suspend fun getRecipeDetail(
        @Path("idLoggedUser") idLoggedUser: Int,
        @Path("recipename") recipeName: String
    ): Response<List<Recipe>>

}