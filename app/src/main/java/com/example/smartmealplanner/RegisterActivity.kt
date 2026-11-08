package com.example.smartmealplanner

import android.util.Log
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartmealplanner.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(){
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val name = binding.fullNameEditText.text.toString().trim()
            val email = binding.registerEmailEditText.text.toString().trim()
            val password = binding.registerPasswordEditText.text.toString()

            if (name.isEmpty() || password.isEmpty()){
                Toast.makeText(this,"Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(this,"Account has been created. Please log in gto continue", Toast.LENGTH_LONG).show()

            val intent = Intent(this,LoginActivity::class.java)

            intent.putExtra("EMAIL",email)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

            startActivity(intent)
        }

        binding.loginLinkTextView.setOnClickListener {
            finish()
        }

    }

    // The screen is now VISIBLE
    override fun onStart() {
        super.onStart()
        Log.d("REGISTER_LIFECYCLE", "RegisterActivity: onStart")
    }
    // The screen is in FRONT and the user can interact with it
    override fun onResume() {
        super.onResume()
        Log.d("REGISTER_LIFECYCLE", "RegisterActivity: onResume")
    }
    // The screen is LEAVING the front (e.g. Home was pressed)
    override fun onPause() {
        super.onPause()
        Log.d("REGISTER_LIFECYCLE", "RegisterActivity: onPause")
    }
    // The screen is NO LONGER VISIBLE
    override fun onStop() {
        super.onStop()
        Log.d("REGISTER_LIFECYCLE", "RegisterActivity: onStop")
    }
    // The user came BACK to a stopped screen
    override fun onRestart() {
        super.onRestart()
        Log.d("REGISTER_LIFECYCLE", "RegisterActivity: onRestart")
    }
    // The screen is being DESTROYED
    override fun onDestroy() {
        super.onDestroy()
        Log.d("REGISTER_LIFECYCLE", "RegisterActivity: onDestroy")
    }

}