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
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
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

    /**
     * Filters a list of active recipes by recipe name.
     * @param idLoggedUser The USER ID of the logged in user.
     * @param recipeName The RECIPE NAME of food.
     * @return A list of [Recipe] containing the filtered list of [Recipe].
     */
    @GET(ConstantNetworkService.FILTER_RECIPE_BY_NAME_ENDPOINT)
    suspend fun filterRecipesByName(
        @Path(value = "idLoggedUser") idLoggedUser: Int,
        @Path(value = "recipename") recipeName: String
    ): Response<List<Recipe>>

    /**
     * Filters a list of active recipes by category.
     * @param idLoggedUser The USER ID of the logged in user.
     * @param categoryId The CATEGORY of food.
     * @return A list of [Recipe] containing the filtered list of [Recipe].
     */
    @GET(ConstantNetworkService.FILTER_RECIPE_BY_CATEGORY_ENDPOINT)
    suspend fun filterRecipesByCategory(
        @Path(value = "idLoggedUser") idLoggedUser: Int,
        @Path(value = "categoryids") categoryId: Int
    ): Response<List<Recipe>>

    /**
     * Like a recipe.
     * @param idLoggedUser The USER ID of the logged in user.
     * @param recipeId The RECIPE ID of the recipe.
     * @return A [Recipe] containing the new information.
     */
    @POST(ConstantNetworkService.LIKE_RECIPE_ENDPOINT)
    suspend fun likeRecipe(
        @Path(value = "idLoggedUser") idLoggedUser: Int,
        @Path(value = "idrecipe") recipeId: Int
    ): Response<Recipe>

    /**
     * Removes a like from the recipe.
     * @param idLoggedUser The USER ID of the logged in user.
     * @param recipeId The RECIPE ID of the recipe.
     * @return A [Recipe] containing the new information.
     */
    @DELETE(ConstantNetworkService.REMOVE_LIKE_RECIPE_ENDPOINT)
    suspend fun removeLikeRecipe(
        @Path(value = "idLoggedUser") idLoggedUser: Int,
        @Path(value = "idrecipe") recipeId: Int
    ): Response<Recipe>

    /**
     * Adds a recipe to my favorites.
     * @param idLoggedUser The USER ID of the logged in user.
     * @param recipeId The RECIPE ID of the recipe.
     * @return A [Recipe] containing the new information.
     */
    @POST(ConstantNetworkService.ADD_FAVORITE_RECIPE_ENDPOINT)
    suspend fun addFavoriteRecipe(
        @Path(value = "idLoggedUser") idLoggedUser: Int,
        @Path(value = "idrecipe") recipeId: Int
    ): Response<Recipe>

    /**
     * Removes a recipe from my favorites.
     * @param idLoggedUser The USER ID of the logged in user.
     * @param recipeId The RECIPE ID of the recipe.
     * @return A [Recipe] containing the new information.
     */
    @DELETE(ConstantNetworkService.REMOVE_FAVORITE_RECIPE_ENDPOINT)
    suspend fun removeFavoriteRecipe(
        @Path(value = "idLoggedUser") idLoggedUser: Int,
        @Path(value = "idrecipe") recipeId: Int
    ): Response<Recipe>
}