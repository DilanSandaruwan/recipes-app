package com.gtp01.group01.android.recipesmobileapp.shared.common

sealed class Result<out R> {

    // Represents a loading state
    data object Loading : Result<Nothing>()

    // Represents a successful result holding the actual value of type R
    data class Success<out R>(val result: R) : Result<R>()

    // Represents a failure state with an associated error message
    data class Failure<out R>(val error: String) : Result<R>()
}