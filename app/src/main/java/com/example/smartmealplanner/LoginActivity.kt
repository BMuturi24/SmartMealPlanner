package com.example.smartmealplanner

import android.net.Uri
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartmealplanner.databinding.ActivityLoginBinding

class LoginActivity: AppCompatActivity(){
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val registeredEmail = intent.getStringExtra("EMAIL")
        if (registeredEmail != null) {
            binding.emailEditText.setText(registeredEmail)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.toString().trim()
            val password = binding.passwordEditText.toString().trim()

            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this,"Please enter your email and password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

            finish()
        }
        binding.registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.helpTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.strathmore.edu"))
            startActivity(intent)
        }
    }

    // The screen is now VISIBLE
    override fun onStart() {
        super.onStart()
        Log.d("LOGIN_LIFECYCLE", "LoginActivity: onStart")
    }
    // The screen is in FRONT and the user can interact with it
    override fun onResume() {
        super.onResume()
        Log.d("LOGIN_LIFECYCLE", "LoginActivity: onResume")
    }
    // The screen is LEAVING the front (e.g. Home was pressed)
    override fun onPause() {
        super.onPause()
        Log.d("LOGIN_LIFECYCLE", "LoginActivity: onPause")
    }
    // The screen is NO LONGER VISIBLE
    override fun onStop() {
        super.onStop()
        Log.d("LOGIN_LIFECYCLE", "LoginActivity: onStop")
    }
    // The user came BACK to a stopped screen
    override fun onRestart() {
        super.onRestart()
        Log.d("LOGIN_LIFECYCLE", "LoginActivity: onRestart")
    }
    // The screen is being DESTROYED
    override fun onDestroy() {
        super.onDestroy()
        Log.d("LOGIN_LIFECYCLE", "LoginActivity: onDestroy")
    }

}