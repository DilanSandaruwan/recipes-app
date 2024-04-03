package com.gtp01.group01.android.recipesmobileapp.constant
/**
 * Singleton object containing constant response codes commonly used in HTTP requests.
 * These codes represent different types of responses such as informational, successful, redirection,
 * client error, and server error responses.
 */
object ConstantResponseCode {
    /***
     * 100 - 199 informational responses
     * 200 - 299 successful responses
     * 300 - 399 redirection messages
     * 400 - 499 client error responses
     * 500 - 599 server error responses
     * IOException representing the IOException message
     * Exception representing the generic Exception message
     * Timeout representing the timeout Exception message
     */
    const val SUCCESS = 200
    const val BAD_REQUEST = 400
    const val UNAUTHORIZED = 401
    const val SERVER_NOT_FOUND = 404
    const val REQUEST_TIMEOUT = 408
    const val URI_TOO_LONG = 414
    const val INTERNAL_SERVER_ERROR = 500
    const val BAD_GATEWAY = 502
    const val SERVICE_UNAVAILABLE = 503
    const val GATEWAY_TIMEOUT = 504
    const val IOEXCEPTION = "IOException"
    const val EXCEPTION = "Exception"
    const val TIMEOUT_EXCEPTION = "Timeout"
}