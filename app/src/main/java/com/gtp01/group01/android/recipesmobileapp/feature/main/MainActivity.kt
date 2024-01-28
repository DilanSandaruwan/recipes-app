package com.gtp01.group01.android.recipesmobileapp.feature.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.gtp01.group01.android.recipesmobileapp.R
import com.gtp01.group01.android.recipesmobileapp.constant.AuthProviders
import com.gtp01.group01.android.recipesmobileapp.constant.AuthProviders.providers
import com.gtp01.group01.android.recipesmobileapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Arrays

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


  //private lateinit var providers: List<AuthUI.IdpConfig>
    private val MY_REQUEST_CODE: Int = 7117
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var menu: Menu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Access the list of providers
         AuthProviders.providers

        showSignInOptions()

        // Initialize the app and UI
        initViews()
    }





    private fun showSignInOptions() {

        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.MyTheme)
                .build(), MY_REQUEST_CODE
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