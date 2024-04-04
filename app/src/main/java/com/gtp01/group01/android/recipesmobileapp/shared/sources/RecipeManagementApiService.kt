package com.gtp01.group01.android.recipesmobileapp.shared.sources

import com.gtp01.group01.android.recipesmobileapp.constant.ConstantNetworkService
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantNetworkService.RAPID_HEADER_1
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantNetworkService.RAPID_HEADER_2
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantNetworkService.RECIPE_BASE_NUTRITION_VALUES_URL
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantNetworkService.RECIPE_GET_CATEGORIES_END_POINT
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantNetworkService.RECIPE_GET_NUTRITION_VALUES_END_POINT
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantNetworkService.RECIPE_POST_RECIPE_END_POINT
import com.gtp01.group01.android.recipesmobileapp.shared.model.FoodCategory
import com.gtp01.group01.android.recipesmobileapp.shared.model.Recipe
import com.gtp01.group01.android.recipesmobileapp.shared.models.NutritionModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET(RECIPE_GET_CATEGORIES_END_POINT)
    suspend fun getCategoryList(): Response<List<FoodCategory>>

    @POST(RECIPE_POST_RECIPE_END_POINT)
    suspend fun saveNewRecipe(
        @Query("idLoggedUser") idLoggedUser: Int,
        @Body recipe: Recipe
    ): Response<Recipe>


    @PUT(ConstantNetworkService.RECIPE_PUT_RECIPE_END_POINT)
    suspend fun updateRecipe(
        @Query("idLoggedUser") idLoggedUser: Int,
        @Body recipe: Recipe
    ): Response<Recipe>

    @GET(ConstantNetworkService.RECIPE_GET_SPECIFIC_RECIPE_END_POINT)
    suspend fun getOneRecipe(
        @Path(value = "idLoggedUser") idLoggedUser: Int,
        @Path(value = "idrecipe") idrecipe: Int
    ): Response<Recipe>


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

    /**
     * Filters a list of active recipes by calorie count.
     * @param idLoggedUser The USER ID of the logged in user.
     * @param maxCalorie The MAXIMUM CALORIE COUNT of food.
     * @return A list of [Recipe] containing the filtered list of [Recipe].
     */
    @GET(ConstantNetworkService.FILTER_RECIPE_BY_CALORIE_ENDPOINT)
    suspend fun filterRecipesByCalorie(
        @Path(value = "idLoggedUser") idLoggedUser: Int,
        @Path(value = "maxcalory") maxCalorie: Int
    ): Response<List<Recipe>>
}