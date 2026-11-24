package com.example.smartmealplanner.ui.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.smartmealplanner.R
import com.example.smartmealplanner.data.local.AppDatabase
import com.example.smartmealplanner.data.repository.MealRepository
import com.example.smartmealplanner.ui.MealReminderWorker
import com.example.smartmealplanner.ui.admin.AdminActivity
import com.example.smartmealplanner.ui.main.MainActivity
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private lateinit var repo: MealRepository

    private lateinit var etLoginId: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvGoRegister: TextView

    // Permission launcher for notifications permission
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notifications enabled!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Bind UI elements manually using findViewById
        etLoginId = findViewById(R.id.etLoginId)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvGoRegister = findViewById(R.id.tvGoRegister)

        // Initialize the Room database and Repository
        val db = AppDatabase.get(this)
        repo = MealRepository(db.userDao(), db.mealDao(), db.clientProfileDao())

        // Request Notification permission and schedule the daily WorkManager task
        // Ask for notification permission if needed
        askNotificationPermission()

        // Schedule the daily reminder
        scheduleDailyReminder()

        btnLogin.setOnClickListener { doLogin() }
        tvGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


    private fun scheduleDailyReminder() {
        // WorkManager schedules a background task to run every 24 hours
        val reminderRequest = PeriodicWorkRequestBuilder<MealReminderWorker>(24, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(this).enqueue(reminderRequest)
    }

    private fun doLogin() {
        val id       = etLoginId.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (id.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter username/email and password", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val user = repo.login(id)
            if (user == null || user.passwordHash != password) {
                Toast.makeText(this@LoginActivity,
                    "Invalid credentials", Toast.LENGTH_SHORT).show()
                return@launch
            }

            // Save the user's ID locally so other screens know who is logged in
            getSharedPreferences("mealplanner", MODE_PRIVATE)
                .edit().putInt("userId", user.id).apply()

            repo.seedIfEmpty()

            Toast.makeText(this@LoginActivity,
                "You are logged in as ${user.username}", Toast.LENGTH_LONG).show()

            // Route the user to different screens based on their roles
            val target = if (user.role == "ADMIN") AdminActivity::class.java
            else MainActivity::class.java
            val i = Intent(this@LoginActivity, target)
            i.putExtra("userId", user.id)
            i.putExtra("username", user.username)
            startActivity(i)
            finish()
        }
    }
}