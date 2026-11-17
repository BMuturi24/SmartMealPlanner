package com.example.smartmealplanner.ui.admin

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smartmealplanner.R

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val tvAdminTitle = findViewById<TextView>(R.id.tvAdminTitle)
        tvAdminTitle.text = "Admin Panel — ${intent.getStringExtra("username") ?: "Admin"}"
    }
}