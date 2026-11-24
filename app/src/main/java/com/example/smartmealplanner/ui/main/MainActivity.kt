package com.example.smartmealplanner.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.smartmealplanner.R
import com.example.smartmealplanner.ui.auth.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val name = intent.getStringExtra("username") ?: "Client"
        Toast.makeText(this, "Welcome $name", Toast.LENGTH_LONG).show()

        val tvHeader = findViewById<TextView>(R.id.tvClientHeader)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavClient)
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController

        // Update header dynamically
        navController.addOnDestinationChangedListener { _, destination, _ ->
            tvHeader.text = when (destination.id) {
                R.id.setupFragment -> "Set Up Your Profile — $name"
                R.id.dashboardFragment -> "Your Meal Plan — $name"
                R.id.profileSummaryFragment -> "Your Profile — $name"
                else -> "Client Panel — $name"
            }
        }

        // Bottom Nav Click Listener
        bottomNav.setOnItemSelectedListener { item ->
            val currentDest = navController.currentDestination?.id

            when (item.itemId) {
                R.id.nav_meals -> {
                    if (currentDest != R.id.dashboardFragment) {
                        // Navigate to dashboard and clear backstack
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.client_nav_graph, true)
                            .build()
                        navController.navigate(R.id.dashboardFragment, null, navOptions)
                    }
                    true
                }
                R.id.nav_profile -> {
                    if (currentDest != R.id.profileSummaryFragment) {
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.client_nav_graph, true)
                            .build()
                        navController.navigate(R.id.profileSummaryFragment, null, navOptions)
                    }
                    true
                }
                R.id.nav_logout_client -> {
                    // Clear prefs and log out
                    getSharedPreferences("mealplanner", MODE_PRIVATE).edit().clear().apply()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}