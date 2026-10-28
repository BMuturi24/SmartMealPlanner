package com.example.smartmealplanner

import  android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.smartmealplanner.databinding.ActivityMainBinding

class  MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.saveButton.setOnClickListener{
            val name = binding.ClientInputText.text.toString()
            val phone = binding.phoneEditText.text.toString()
            val email = binding.clientEmail.text.toString()
            val budget = binding.budgetEditText.text.toString()
            val client = Client(name,phone,email,budget)
            binding.client = client
        }


    }
}