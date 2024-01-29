package com.gtp01.group01.android.recipesmobileapp.constant

import android.app.Activity
import com.firebase.ui.auth.AuthUI
import com.gtp01.group01.android.recipesmobileapp.R

object AuthUtils {
    fun showSignInOptions(activity: Activity, requestCode: Int, providers: List<AuthUI.IdpConfig>) {
        activity.startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.MyTheme)
                .build(), requestCode
        )
    }
}