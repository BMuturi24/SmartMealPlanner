package com.example.smartmealplanner.ui.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smartmealplanner.R
import com.example.smartmealplanner.data.local.AppDatabase
import com.example.smartmealplanner.data.model.User
import com.example.smartmealplanner.data.repository.MealRepository
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var repo: MealRepository

    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var rbAdmin: RadioButton
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        rbAdmin = findViewById(R.id.rbAdmin)
        btnRegister = findViewById(R.id.btnRegister)

        val db = AppDatabase.get(this)
        repo = MealRepository(db.userDao(), db.mealDao(), db.clientProfileDao())

        btnRegister.setOnClickListener { doRegister() }
    }

    private fun doRegister() {
        val username = etUsername.text.toString().trim()
        val email    = etEmail.text.toString().trim()
        val pwd1     = etPassword.text.toString()
        val pwd2     = etConfirmPassword.text.toString()
        val role     = if (rbAdmin.isChecked) "ADMIN" else "CLIENT"

        // Validate that no fields are empty and passwords match before saving info to database
        if (username.isEmpty() || email.isEmpty() || pwd1.isEmpty()) {
            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show()
            return
        }
        if (pwd1 != pwd2) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                repo.register(User(
                    username = username, email = email,
                    passwordHash = pwd1, role = role
                ))
                Toast.makeText(this@RegisterActivity,
                    "Registered! Please log in.", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                // Catch database errors (e.g., duplicate username)
                Toast.makeText(this@RegisterActivity,
                    "Registration failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}