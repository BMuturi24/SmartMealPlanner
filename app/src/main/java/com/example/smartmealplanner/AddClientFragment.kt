package com.example.smartmealplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartmealplanner.databinding.FragmentAddClientBinding
import com.example.smartmealplanner.databinding.FragmentDashboardBinding

class AddClientFragment : Fragment() {

    // _binding can be null (no view yet, or view destroyed)
    private var _binding: FragmentDashboardBinding? = null
    // binding is a shortcut we only use while the view exists
    private val binding get() = _binding!!
    // Build this screen's view from fragment_dashboard.xml
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root // hand the view to the NavHost to display
    }
    // The view now exists: fill in the metrics
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
// Read from the shared list of tenants
        val clientCount = ClientsRepository.client.size
        val totalBudget = ClientsRepository.totalBudget()
// tenantCountTextView ← android:id="@+id/tenantCountTextView"
        binding.clientCountTextView.text = "Clients\n$clientCount"
// totalRentTextView ← android:id="@+id/totalRentTextView"
        binding.totalBudgetTextView.text = "Total budget\nKSh %,.0f".format(totalBudget)
    }
    // The view is destroyed: let go of it
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}