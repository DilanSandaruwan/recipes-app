package com.gtp01.group01.android.recipesmobileapp.sources

import com.gtp01.group01.android.recipesmobileapp.constant.ConstantNetworkService
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantNetworkService.RAPID_HEADER_1
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantNetworkService.RAPID_HEADER_2
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantNetworkService.RECIPE_BASE_NUTRITION_VALUES_URL
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantNetworkService.RECIPE_GET_NUTRITION_VALUES_END_POINT
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import com.gtp01.group01.android.recipesmobileapp.shared.models.NutritionModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface for managing recipe-related API calls.
 */
interface RecipeManagementApiService {
    /**
     * Fetches nutrition information based on the provided query.
     *
     * @param query The query string for retrieving nutrition information.
     * @return A [Response] containing a list of [NutritionModel] objects representing the retrieved nutrition information.
     */
    @Headers(
        RAPID_HEADER_1, RAPID_HEADER_2
    )
    @GET("$RECIPE_BASE_NUTRITION_VALUES_URL$RECIPE_GET_NUTRITION_VALUES_END_POINT")
    suspend fun getNutritions(@Query("query") query: String)
            : Response<List<NutritionModel>>

    /**
     * Filters a list of active recipes by preparation time.
     * @param idLoggedUser The USER ID of the logged in user.
     * @param maxduration The MAXIMUM DURATION of food preparation time.
     * @return A list of [Recipe] containing the filtered list of [Recipe].
     */
    @GET(ConstantNetworkService.FILTER_RECIPE_BY_DURATION_ENDPOINT)
    suspend fun filterRecipesByDuration(
        @Path(value = "idLoggedUser") idLoggedUser: Int,
        @Path(value = "maxduration") maxduration: Int
    ): Response<List<Recipe>>
}