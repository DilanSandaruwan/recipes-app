package com.gtp01.group01.android.recipesmobileapp.shared.common
/**
 * Sealed class representing different states of an asynchronous operation.
 */
sealed class Result {

    // Represents a loading state
    object Loading : Result()

    /**
     * Represents a successful result holding the actual value of type R.
     * @param result The result of the operation.
     */
    data class Success<out R>(val result: R) : Result()

    /**
     * Represents a failure state with an associated error message.
     * @param error The error message describing the failure.
     */
    data class Failure(val error: String) : Result()

}