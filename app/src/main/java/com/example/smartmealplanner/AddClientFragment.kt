package com.example.smartmealplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smartmealplanner.databinding.FragmentAddClientBinding
import com.example.smartmealplanner.model.Administrator

/** ADMIN: create a client account (the client can then log in with these details). */
class AddClientFragment : Fragment() {
    private var _binding: FragmentAddClientBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddClientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val admin = Session.account as Administrator
        val host = requireActivity() as MainActivity

        binding.saveButton.setOnClickListener {
            admin.addClient(
                Session.repo,
                binding.usernameInput.text.toString(),
                binding.emailInput.text.toString(),
                binding.passwordInput.text.toString()
            ).onSuccess {
                Toast.makeText(requireContext(), "Client added", Toast.LENGTH_SHORT).show()
                host.back()
            }.onFailure { Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show() }
        }
        binding.cancelButton.setOnClickListener { host.back() }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}