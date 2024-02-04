package com.gtp01.group01.android.recipesmobileapp.feature.main

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.constant.AuthProviders.providers
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantRequestCode.MY_REQUEST_CODE
import com.gtp01.group01.android.recipesmobileapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var menu: Menu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Access the list of providers
        providers
        showSignInOptions()
        // Initialize the app and UI
        initViews()
    }

    /**
     * Displays the Firebase Authentication sign-in options.
     *
     * This function starts the Firebase Authentication UI flow to allow users to sign in using various
     * authentication providers. It uses a custom theme (if provided) and sets the available authentication
     * providers based on the 'providers' property.
     *
     * @see AuthUI
     * @see AuthUI.IdpConfig
     * @see MY_REQUEST_CODE
     */
    private fun showSignInOptions() {
        startActivityForResult(
            // Set the available authentication providers (e.g., email/password, Google, etc.)
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                // Set a custom theme for the authentication UI (optional)
                .setTheme(R.style.MyTheme)
                .build(),
            MY_REQUEST_CODE// Pass the request code to identify the result in onActivityResult
        )
    }
    /**
     * Initialize the UI components and setup navigation.
     */
    private fun initViews() {
        DataBindingUtil.setContentView<ActivityMainBinding?>(this, R.layout.activity_main).apply {
            binding = this
            setUpNavController()
        }
    }

    /***
     * Set up the navigation controller for the bottom navigation menu
     */
    private fun setUpNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

        navHostFragment.navController.let {
            binding.navView.apply {
                bottomNavView = this
                this@MainActivity.menu = this.menu
                setupWithNavController(it)
            }
        }
    }
}