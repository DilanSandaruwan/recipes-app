package com.gtp01.group01.android.recipesmobileapp.shared.sources

import com.gtp01.group01.android.recipesmobileapp.constant.ConstantNetworkService
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import com.gtp01.group01.android.recipesmobileapp.shared.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interface defining API endpoints related to authentication and recipe details.
 */
interface AuthApiService {
    /**
     * Save user details to the backend.
     *
     * @param authUser The [AuthUser] object containing user details.
     * @return A [Response] indicating the success or failure of the save operation.
     */
    @Headers(ConstantNetworkService.ACCEPT_JSON_UTF8)
    @POST(ConstantNetworkService.AUTH_DETAIL_USER_ENDPOINT)
    suspend fun saveUser(@Body authUser: User)
            : Response<User>

    /**
     * Fetches recipe details for the specified user and recipe ID.
     *
     * @param idLoggedUser The ID of the logged-in user.
     * @param idrecipe The ID of the recipe to fetch details for.
     * @return A [Response] containing the details of the recipe if successful, otherwise an error response.
     */
    @Headers(ConstantNetworkService.ACCEPT_JSON_UTF8)
    @GET(ConstantNetworkService.RECIPE_DETAIL_BY_RECIPE_id_ENDPOINT)
    suspend fun getRecipeDetail(
        @Path("idLoggedUser") idLoggedUser: Int,
        @Path("idrecipe") idrecipe: Int
    ): Response<Recipe>

}