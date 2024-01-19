package com.gtp01.group01.android.recipesmobileapp.constant

object ConstantResponseCode {
    /***
     * 100 - 199 informational responses
     * 200 - 299 successful responses
     * 300 - 399 redirection messages
     * 400 - 499 client error responses
     * 500 - 599 server error responses
     */
    val SUCCESS = 200
    val BAD_REQUEST = 400
    val UNAUTHORIZED = 401
    val SERVER_NOT_FOUND = 404
    val REQUEST_TIMEOUT = 408
    val URI_TOO_LONG = 414
    val INTERNAL_SERVER_ERROR = 500
}