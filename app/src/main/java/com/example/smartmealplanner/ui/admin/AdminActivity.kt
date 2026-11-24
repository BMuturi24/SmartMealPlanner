package com.example.smartmealplanner.ui.admin

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smartmealplanner.R

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

       // Retrieve the admin's name passed from the Login screen via Intent
        val tvAdminTitle = findViewById<TextView>(R.id.tvAdminTitle)
        tvAdminTitle.text = "Admin Panel — ${intent.getStringExtra("username") ?: "Admin"}"
    }
}