package com.gtp01.group01.android.recipesmobileapp.constant
/**
 * Object containing constants related to the application's network service, including base URL and endpoints.
 */
object ConstantNetworkService {
    /**
     * Value for the Accept header indicating JSON content with UTF-8 encoding.
     */
    const val ACCEPT_JSON_UTF8 = "Accept: application/json; utf-8"
    /**
     * The base URL of the application's API. This URL is used to make network requests.
     */
    const val  BASE_URL= "http://ec2-54-234-237-74.compute-1.amazonaws.com:5000"
    /**
     * Endpoint for user authorization-related save new user API requests.
     */
    const val AUTH_DETAIL_USER_ENDPOINT ="/api/v1/user"
    /**
     * Endpoint for retrieving recipe details by recipe ID.
     * Use placeholders `{idLoggedUser}` and `{idrecipe}` to specify user ID and recipe ID respectively.
     */
    const val RECIPE_DETAIL_BY_RECIPE_id_ENDPOINT= "api/v1/reader/{idLoggedUser}/recipe/id/{idrecipe}"
    /***
     * Endpoints for Add New Recipe
     */
    const val RAPID_HEADER_1 = "X-RapidAPI-Key:e32aa9fad9msh634c274ed26adcap18574fjsn0d69ac29cb04"
    const val RAPID_HEADER_2 = "X-RapidAPI-Host:nutrition-by-api-ninjas.p.rapidapi.com"
    const val RECIPE_BASE_NUTRITION_VALUES_URL = "https://nutrition-by-api-ninjas.p.rapidapi.com/"
    const val RECIPE_GET_NUTRITION_VALUES_END_POINT = "v1/nutrition?query="

}