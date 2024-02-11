package com.gtp01.group01.android.recipesmobileapp.data

/**
 * Data class representing user authentication details.
 *
 * @property iduser The user's identifier.
 * @property email The user's email address.
 * @property fullname The user's full name.
 */

data class AuthUser(
    val iduser: Int,
    val email: String,
    val fullname: String
)