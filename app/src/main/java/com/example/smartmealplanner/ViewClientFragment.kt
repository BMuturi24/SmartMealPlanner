package com.example.smartmealplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.smartmealplanner.databinding.FragmentViewClientsBinding
import com.example.smartmealplanner.model.Administrator

/** ADMIN: list every client with their budget and goal, and delete a client after confirming. */
class ViewClientFragment : Fragment() {
    private var _binding: FragmentViewClientsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentViewClientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.backButton.setOnClickListener { (requireActivity() as MainActivity).back() }
        refresh()
    }

    private fun refresh() {
        val admin = Session.account as Administrator
        val clients = Session.repo.getClients()
        binding.titleText.text = "Clients (${clients.size})"
        binding.emptyText.visibility = if (clients.isEmpty()) View.VISIBLE else View.GONE
        val box = binding.clientsContainer
        box.removeAllViews()

        clients.forEach { c ->
            val p = c.profile
            box.addCard {
                addText(c.username, 16f, bold = true)
                addText(c.email, 13f)
                addText("Budget ${kes(p.weeklyBudget.toDouble())} per week • ${p.goal.label} • ${p.servings} serving(s)", 12f, topDp = 4)
                addView(Button(context).apply {
                    text = "Delete client"
                    setOnClickListener {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Delete ${c.username}?")
                            .setMessage("This removes the client's account and saved preferences.")
                            .setPositiveButton("Delete") { _, _ -> admin.removeClient(Session.repo, c.id); refresh() }
                            .setNegativeButton("Cancel", null)
                            .show()
                    }
                })
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}