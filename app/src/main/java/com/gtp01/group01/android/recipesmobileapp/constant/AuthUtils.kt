package com.gtp01.group01.android.recipesmobileapp.constant

import android.app.Activity
import com.firebase.ui.auth.AuthUI
import com.gtp01.group01.android.recipesmobileapp.R
/**
 * Utility object for handling authentication-related operations, including showing sign-in options.
 */
object AuthUtils {
    /**
     * Shows the sign-in options for the user.
     *
     * @param activity The [Activity] where the sign-in options should be displayed.
     * @param requestCode The request code used for identifying the result of the sign-in operation.
     * @param providers The list of authentication providers to be displayed.
     */
    fun showSignInOptions(activity: Activity, requestCode: Int, providers: List<AuthUI.IdpConfig>) {
        activity.startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.MyTheme)
                .build(), requestCode
        )
    }
}