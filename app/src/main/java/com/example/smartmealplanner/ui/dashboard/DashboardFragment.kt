package com.example.smartmealplanner.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.smartmealplanner.R
import com.example.smartmealplanner.data.local.AppDatabase
import com.example.smartmealplanner.data.repository.MealRepository
import com.example.smartmealplanner.ui.viewmodel.MealViewModel

class DashboardFragment : Fragment() {

    private var userId: Int = 0
    private lateinit var tvRecommendations: TextView

    private val vm: MealViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val db = AppDatabase.get(requireContext())
                return MealViewModel(
                    MealRepository(db.userDao(), db.mealDao(), db.clientProfileDao())
                ) as T
            }
        }
    }

    override fun onCreateView(
        i: LayoutInflater, c: ViewGroup?, s: Bundle?
    ): View? {
        return i.inflate(R.layout.fragment_dashboard, c, false)
    }

    override fun onViewCreated(view: View, s: Bundle?) {
        super.onViewCreated(view, s)

        tvRecommendations = view.findViewById(R.id.tvRecommendations)
        val btnGenerate = view.findViewById<Button>(R.id.btnGenerate)
        val btnViewProfile = view.findViewById<Button>(R.id.btnViewProfile)

        userId = requireContext()
            .getSharedPreferences("mealplanner", 0)
            .getInt("userId", 0)

        vm.recommendations.observe(viewLifecycleOwner) { meals ->
            tvRecommendations.text = if (meals.isEmpty()) {
                "No meals generated yet. Hit the button below!"
            } else {
                meals.joinToString("\n\n") {
                    "${it.name}\n   ${it.calories} kcal  •  KES ${it.costKes}"
                }
            }
        }

        vm.message.observe(viewLifecycleOwner) { m ->
            m?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                vm.consumeMessage()
            }
        }

        // GENERATE MEALS -> Fetches profile from DB and generates the plan
        btnGenerate.setOnClickListener {
            vm.loadProfile(userId)
            Toast.makeText(requireContext(), "Meals generated!", Toast.LENGTH_SHORT).show()
        }

        // VIEW PROFILE -> Navigate back to Summary
        btnViewProfile.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_summary)
        }
    }
}