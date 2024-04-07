package com.gtp01.group01.android.recipesmobileapp.feature.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.constant.AuthProviders.providers
import com.gtp01.group01.android.recipesmobileapp.constant.ConstantRequestCode.MY_REQUEST_CODE
import com.gtp01.group01.android.recipesmobileapp.databinding.ActivityMainBinding
import com.gtp01.group01.android.recipesmobileapp.shared.sources.Local.LocalDataSource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel
    lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var menu: Menu

    // Inject LocalDataSource
    @Inject
    lateinit var localDataSource: LocalDataSource
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        // Access the list of providers
        providers
        showSignInOptions()
        // Initialize the app and UI
        initViews()
        eventListeners()

    }

    /**
     * Handle the result of Firebase Authentication sign-in.
     */
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Sign-in successful, retrieve user ID and save it
                val currentUser = FirebaseAuth.getInstance().currentUser
                val userId = currentUser?.uid
                userId?.let {
                    val userIdInt = it.toIntOrNull()
                        ?: return@let  // Convert to Int, or return if conversion fails
                    localDataSource.saveUserId(userIdInt)
                }  // Call the saveUser function
                viewModel.saveUser()
            } else {
                // Sign-in failed or cancelled
                // Handle the failure or cancellation
                showPopup(
                    1, getString(R.string.popup_title_error),
                    getString(R.string.popup_message_guest)
                )
            }
        }
    }

    /**
     * Sets up event listeners for UI components.
     */
    private fun eventListeners() {
        binding.lytPopupIncluded.ivPopupClose.setOnClickListener {
            binding.lytPopupIncluded.lytPopupScreen.visibility = View.GONE
            binding.navView.visibility = View.VISIBLE
        }
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

    /**
     * Displays a popup message.
     */
    fun showPopup(type: Int, title: String, message: String) {

        var icon: Int = R.drawable.ic_info_popup
        when (type) {
            0 -> {
                icon = R.drawable.ico_selected_item
            }

            1 -> {
                icon = R.drawable.ic_error_popup
            }

            2 -> {
                icon = R.drawable.ic_info_popup
            }
        }
        binding.lytPopupIncluded.ivPopupIcon.setImageResource(icon)
        binding.lytPopupIncluded.mtvPopupTitle.text = title
        binding.lytPopupIncluded.mtvPopupDescription.text = message
        binding.lytPopupIncluded.lytPopupScreen.visibility = View.VISIBLE
        binding.navView.visibility = View.GONE
    }
}