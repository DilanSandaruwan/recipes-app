package com.gtp01.group01.android.recipesmobileapp.constant

import com.firebase.ui.auth.AuthUI

object AuthProviders {
    val providers: List<AuthUI.IdpConfig> = listOf(
        AuthUI.IdpConfig.AnonymousBuilder().build(),
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
    )
}