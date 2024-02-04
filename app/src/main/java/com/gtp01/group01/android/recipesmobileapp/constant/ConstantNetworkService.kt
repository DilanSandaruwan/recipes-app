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
     * Endpoint for user-related API requests.
     */
    const val REPOSITORIES_ENDPOINT ="/api/v1/user"
}