package com.example.smartmealplanner.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.smartmealplanner.R
import com.example.smartmealplanner.ui.auth.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val tvAdminTitle = findViewById<TextView>(R.id.tvAdminTitle)
        tvAdminTitle.text = "Admin Panel — ${intent.getStringExtra("username") ?: "Admin"}"

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavAdmin)
        val navHost = supportFragmentManager.findFragmentById(R.id.adminNavHost) as NavHostFragment
        val navController = navHost.navController

        bottomNav.setOnItemSelectedListener { item ->
            val currentDest = navController.currentDestination?.id

            when (item.itemId) {
                R.id.nav_view_clients -> {
                    if (currentDest != R.id.viewClientFragment) {
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.admin_nav_graph, true)
                            .build()
                        navController.navigate(R.id.viewClientFragment, null, navOptions)
                    }
                    true
                }
                R.id.nav_add_client -> {
                    if (currentDest != R.id.addClientFragment) {
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.admin_nav_graph, true)
                            .build()
                        navController.navigate(R.id.addClientFragment, null, navOptions)
                    }
                    true
                }
                R.id.nav_logout_admin -> {
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