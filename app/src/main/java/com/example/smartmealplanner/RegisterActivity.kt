package com.example.smartmealplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartmealplanner.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Session.init(this)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val password = binding.passwordInput.text.toString()
            // Asking twice means a typo here can't lock the client out at login.
            if (password != binding.confirmInput.text.toString()) {
                Toast.makeText(this, "The two passwords don't match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Session.repo.createAccount(binding.usernameInput.text.toString(), binding.emailInput.text.toString(), password)
                .onSuccess { account ->
                    Session.account = account
                    Session.loginName = account.username
                    Toast.makeText(this, "You are logged in as ${account.username}", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, ClientHomeActivity::class.java))
                    finishAffinity()
                }
                .onFailure { Toast.makeText(this, it.message, Toast.LENGTH_LONG).show() }
        }
        binding.backToLoginButton.setOnClickListener { finish() }
    }
}