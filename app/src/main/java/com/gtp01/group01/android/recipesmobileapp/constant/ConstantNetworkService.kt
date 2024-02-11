package com.gtp01.group01.android.recipesmobileapp.constant
/**
 * Object containing constants related to the application's network service, including base URL and endpoints.
 */
object ConstantNetworkService {
    /**
     * The base URL of the application's API. This URL is used to make network requests.
     */
    const val  BASE_URL= "http://ec2-52-207-150-55.compute-1.amazonaws.com:5000"
    /**
     * Endpoint for user authorization-related save new user API requests.
     */
    const val AUTH_DETAIL_USER_ENDPOINT ="/api/v1/user"

    /***
     * Endpoints for Add New Recipe
     */
    const val RAPID_HEADER_1 = "X-RapidAPI-Key:e32aa9fad9msh634c274ed26adcap18574fjsn0d69ac29cb04"
    const val RAPID_HEADER_2 = "X-RapidAPI-Host:nutrition-by-api-ninjas.p.rapidapi.com"
    const val RECIPE_BASE_NUTRITION_VALUES_URL = "https://nutrition-by-api-ninjas.p.rapidapi.com/"
    const val RECIPE_GET_NUTRITION_VALUES_END_POINT = "v1/nutrition?query="

}