package com.example.smartmealplanner.ui.clients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.smartmealplanner.R
import com.example.smartmealplanner.data.local.AppDatabase
import com.example.smartmealplanner.data.repository.MealRepository
import kotlinx.coroutines.launch

class ProfileSummaryFragment : Fragment() {

    override fun onCreateView(
        i: LayoutInflater, c: ViewGroup?, s: Bundle?
    ): View? {
        return i.inflate(R.layout.fragment_profile_summary, c, false)
    }

    override fun onViewCreated(view: View, s: Bundle?) {
        super.onViewCreated(view, s)

        val tvSummary = view.findViewById<TextView>(R.id.tvSummary)
        val btnViewMeals = view.findViewById<Button>(R.id.btnViewMeals)
        val btnEditProfile = view.findViewById<Button>(R.id.btnEditProfile)

        val userId = requireContext()
            .getSharedPreferences("mealplanner", 0)
            .getInt("userId", 0)

        val db = AppDatabase.get(requireContext())
        val repo = MealRepository(db.userDao(), db.mealDao(), db.clientProfileDao())

        lifecycleScope.launch {
            val e = repo.profileFor(userId)
            tvSummary.text = if (e == null) {
                "No profile set yet."
            } else {
                """
                Your Profile

                Goal         : ${e.healthGoal}
                Budget/day   : KES ${e.budgetPerDay}
                Diet         : ${e.dietaryPreference}
                Description  : ${e.description.ifBlank { "—" }}
                """.trimIndent()
            }
        }

        // VIEW MEAL PLAN -> Goes to Dashboard
        btnViewMeals.setOnClickListener {
            findNavController().navigate(R.id.action_summary_to_dashboard)
        }

        // EDIT PROFILE -> Goes to Setup
        btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_summary_to_setup)
        }
    }
}

