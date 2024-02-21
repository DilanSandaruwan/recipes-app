package com.gtp01.group01.android.recipesmobileapp.shared.common

sealed class Result{


    // Represents a loading state
    object Loading : Result()
    // Represents a successful result holding the actual value of type R
    data class Success<out R>(val result: R) : Result()
    // Represents a failure state with an associated error message
    data class Failure(val error: String) : Result()

}