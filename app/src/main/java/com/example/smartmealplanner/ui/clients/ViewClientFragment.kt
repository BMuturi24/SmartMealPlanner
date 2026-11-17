package com.example.smartmealplanner.ui.clients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.smartmealplanner.R
import com.example.smartmealplanner.data.local.AppDatabase
import com.example.smartmealplanner.data.model.ClientProfileEntity
import com.example.smartmealplanner.data.model.User
import com.example.smartmealplanner.data.repository.MealRepository
import com.example.smartmealplanner.ui.viewmodel.ClientViewModel

class ViewClientFragment : Fragment() {

    private lateinit var tvClients: TextView
    private lateinit var btnRefresh: Button
    private lateinit var btnGoAdd: Button
    private lateinit var etDeleteUsername: EditText
    private lateinit var btnDelete: Button

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
        return i.inflate(R.layout.fragment_view_client, c, false)
    }

    override fun onViewCreated(view: View, s: Bundle?) {
        super.onViewCreated(view, s)

        tvClients = view.findViewById(R.id.tvClients)
        btnRefresh = view.findViewById(R.id.btnRefresh)
        btnGoAdd = view.findViewById(R.id.btnGoAdd)
        etDeleteUsername = view.findViewById(R.id.etDeleteUsername)
        btnDelete = view.findViewById(R.id.btnDelete)

        vm.clients.observe(viewLifecycleOwner) { clients ->
            tvClients.text = render(clients, vm.profiles.value ?: emptyList())
        }
        vm.profiles.observe(viewLifecycleOwner) { profiles ->
            tvClients.text = render(vm.clients.value ?: emptyList(), profiles)
        }

        vm.message.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                vm.consumeMessage()
            }
        }

        btnRefresh.setOnClickListener { vm.refresh() }
        btnDelete.setOnClickListener {
            val username = etDeleteUsername.text.toString().trim()
            if (username.isBlank()) {
                Toast.makeText(requireContext(), "Enter a username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            vm.deleteClient(username)
            etDeleteUsername.text?.clear()
        }

        btnGoAdd.setOnClickListener {
            findNavController().navigate(R.id.action_view_to_add)
        }

        vm.refresh()
    }

    private fun render(clients: List<User>, profiles: List<ClientProfileEntity>): String {
        if (clients.isEmpty()) return "No clients registered yet."
        val map = profiles.associateBy { it.userId }
        return clients.joinToString("\n\n") { u ->
            val p = map[u.id]
            """
            • ${u.username}  (${u.email})
               Goal: ${p?.healthGoal ?: "not set"}
               Budget: KES ${p?.budgetPerDay?.toString() ?: "—"}/day
               Diet: ${p?.dietaryPreference ?: "—"}
               Notes: ${p?.description?.ifBlank { "—" } ?: "—"}
            """.trimIndent()
        }
    }
}