package com.gtp01.group01.android.recipesmobileapp

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gtp01.group01.android.recipesmobileapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var menu: Menu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the app and UI
        initViews()
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