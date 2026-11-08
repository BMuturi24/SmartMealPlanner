package com.example.smartmealplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartmealplanner.databinding.ActivityLoginBinding
import com.example.smartmealplanner.model.Administrator

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Session.init(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val identifier = binding.identifierInput.text.toString().trim()
            val password = binding.passwordInput.text.toString()   // not trimmed: must match what was registered
            if (identifier.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter your username or email and your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Session.repo.login(identifier, password)
                .onSuccess { account ->
                    Session.account = account
                    Session.loginName = identifier
                    Toast.makeText(this, "You are logged in as $identifier", Toast.LENGTH_LONG).show()
                    // Admins and clients see different screens
                    val next = if (account is Administrator) MainActivity::class.java else ClientHomeActivity::class.java
                    startActivity(Intent(this, next))
                    finish()
                }
                .onFailure { Toast.makeText(this, it.message, Toast.LENGTH_LONG).show() }
        }
        binding.goRegisterButton.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
    }
}