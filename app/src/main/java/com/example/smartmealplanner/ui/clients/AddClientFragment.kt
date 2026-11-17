package com.example.smartmealplanner.ui.clients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.smartmealplanner.R
import com.example.smartmealplanner.data.local.AppDatabase
import com.example.smartmealplanner.data.repository.MealRepository
import com.example.smartmealplanner.ui.viewmodel.ClientViewModel

class AddClientFragment : Fragment() {

    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSaveClient: Button
    private lateinit var btnBack: Button

    private val vm: ClientViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val db = AppDatabase.get(requireContext())
                return ClientViewModel(
                    MealRepository(db.userDao(), db.mealDao(), db.clientProfileDao())
                ) as T
            }
        }
    }

    override fun onCreateView(
        i: LayoutInflater, c: ViewGroup?, s: Bundle?
    ): View? {
        return i.inflate(R.layout.fragment_add_client, c, false)
    }

    override fun onViewCreated(view: View, s: Bundle?) {
        super.onViewCreated(view, s)

        etUsername = view.findViewById(R.id.etUsername)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        btnSaveClient = view.findViewById(R.id.btnSaveClient)
        btnBack = view.findViewById(R.id.btnBack)

        vm.message.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                vm.consumeMessage()
            }
        }

        btnSaveClient.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email    = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            if (username.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            vm.addClient(username, email, password)
            etUsername.text?.clear()
            etEmail.text?.clear()
            etPassword.text?.clear()
        }

        btnBack.setOnClickListener { findNavController().popBackStack() }
    }
}