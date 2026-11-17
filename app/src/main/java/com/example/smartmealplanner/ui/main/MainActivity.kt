package com.example.smartmealplanner.ui.main

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.smartmealplanner.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val name = intent.getStringExtra("username") ?: "Client"
        Toast.makeText(this, "Welcome $name", Toast.LENGTH_LONG).show()

        val tvHeader = findViewById<TextView>(R.id.tvClientHeader)
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // Update the title bar based on which screen the user is on
        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            tvHeader.text = when (destination.id) {
                R.id.setupFragment -> "Set Up Your Profile — $name"
                R.id.dashboardFragment -> "Your Meal Plan — $name"
                R.id.profileSummaryFragment -> "Your Profile — $name"
                else -> "Client Panel — $name"
            }
        }
    }
}