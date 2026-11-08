package com.example.smartmealplanner

import android.util.Log
import  android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.smartmealplanner.databinding.ActivityMainBinding

class  MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var lastClient: Client? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MAIN_LIFECYCLE", "MainActivity: onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ── RESTORE STATE (only after a rotation) ─────────────────
        if (savedInstanceState != null) {
            val name = savedInstanceState.getString("client_name")
            val phone = savedInstanceState.getString("client_phone")
            val email = savedInstanceState.getString("client_email")
            val budget = savedInstanceState.getString("client_budget")
            if (name != null && phone != null && email != null && budget != null) {
                val client = Client(name, phone, email, budget)
                lastClient = client
                binding.client = client
            }
        }

        binding.saveButton.setOnClickListener {
            val name = binding.ClientInputText.text.toString()
            val phone = binding.phoneEditText.text.toString()
            val email = binding.clientEmail.text.toString()
            val budget = binding.budgetEditText.text.toString()
            val client = Client(name, phone, email, budget)
            binding.client = client // Data Binding shows the summary
             lastClient = client // remember it for CALL TENANT
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        var userEmail = intent.getStringExtra("USER_EMAIL")
        if (userEmail.equals(userEmail)){
            Toast.makeText(this," You are logged in as $userEmail", Toast.LENGTH_LONG).show()
        }

        binding.saveButton.setOnClickListener{
            val name = binding.ClientInputText.text.toString()
            val phone = binding.phoneEditText.text.toString()
            val email = binding.clientEmail.text.toString()
            val budget = binding.budgetEditText.text.toString()
            val client = Client(name,phone,email,budget)

            if(name.isEmpty() || phone.isEmpty() || email.isEmpty() || budget.isEmpty()){
                Toast.makeText(this,"All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding.client = client
        }

    }

    // The screen is now VISIBLE
    override fun onStart() {
        super.onStart()
        Log.d("MAIN_LIFECYCLE", "MainActivity: onStart")
    }
    // The screen is in FRONT and the user can interact with it
    override fun onResume() {
        super.onResume()
        Log.d("MAIN_LIFECYCLE", "MainActivity: onResume")
    }
    // The screen is LEAVING the front (e.g. Home was pressed)
    override fun onPause() {
        super.onPause()
        Log.d("MAIN_LIFECYCLE", "MainActivity: onPause")
    }
    // The screen is NO LONGER VISIBLE
    override fun onStop() {
        super.onStop()
        Log.d("MAIN_LIFECYCLE", "MainActivity: onStop")
    }
    // The user came BACK to a stopped screen
    override fun onRestart() {
        super.onRestart()
        Log.d("MAIN_LIFECYCLE", "MainActivity: onRestart")
    }
    // The screen is being DESTROYED
    override fun onDestroy() {
        super.onDestroy()
        Log.d("MAIN_LIFECYCLE", "MainActivity: onDestroy")
    }

}