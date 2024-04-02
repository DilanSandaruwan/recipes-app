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
     */
    val SUCCESS = 200
    val BAD_REQUEST = 400
    val UNAUTHORIZED = 401
    val SERVER_NOT_FOUND = 404
    val REQUEST_TIMEOUT = 408
    val URI_TOO_LONG = 414
    val INTERNAL_SERVER_ERROR = 500
    val IOEXCEPTION = "IOException"
    val EXCEPTION = "Exception"
}