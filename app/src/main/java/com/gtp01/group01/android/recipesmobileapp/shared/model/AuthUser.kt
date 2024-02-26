package com.gtp01.group01.android.recipesmobileapp.shared.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing user authentication details.
 *
 * @property iduser The user's identifier.
 * @property email The user's email address.
 * @property fullname The user's full name.
 */

data class AuthUser(
    @SerializedName("iduser") val idUser: Int,
    @SerializedName("email") val email: String,
    @SerializedName("fullname") val fullName: String,

)