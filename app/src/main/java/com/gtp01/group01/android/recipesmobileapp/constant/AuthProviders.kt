package com.gtp01.group01.android.recipesmobileapp.constant

import com.firebase.ui.auth.AuthUI

/**
 * Object providing a list of authentication providers for sign-in options.
 */
object AuthProviders {
    /**
     * List of authentication providers for sign-in options.
     * This list includes options for anonymous, email, and Google sign-in.
     */
    val providers: List<AuthUI.IdpConfig> = listOf(
        AuthUI.IdpConfig.AnonymousBuilder().build(),
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
    )
}