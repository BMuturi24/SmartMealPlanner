package com.example.smartmealplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartmealplanner.databinding.FragmentDashboardBinding

/** ADMIN dashboard: overview numbers from the database and shortcuts to each admin task. */
class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val repo = Session.repo
        val host = requireActivity() as MainActivity

        binding.welcomeText.text = "Welcome ${Session.loginName}"
        binding.clientsCount.text = repo.clientCount().toString()
        binding.budgetTotal.text = kes(repo.totalClientBudget().toDouble())
        binding.foodsCount.text = repo.foodCount().toString()

        binding.addClientButton.setOnClickListener { host.open(AddClientFragment()) }
        binding.viewClientsButton.setOnClickListener { host.open(ViewClientFragment()) }
        binding.manageFoodsButton.setOnClickListener { host.open(FoodsFragment()) }
        binding.logoutButton.setOnClickListener { host.logout() }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}