package com.gtp01.group01.android.recipesmobileapp.constant

sealed class UIState {

    object Loading : UIState()
    data class Success<out R>(val result: R) : UIState()
    data class Failure(val error: String) : UIState()
}