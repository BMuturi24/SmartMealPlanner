package com.example.smartmealplanner.ui.clients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.smartmealplanner.R
import com.example.smartmealplanner.data.local.AppDatabase
import com.example.smartmealplanner.data.model.ClientProfileEntity
import com.example.smartmealplanner.data.repository.MealRepository
import kotlinx.coroutines.launch

class SetupFragment : Fragment() {

    private lateinit var repo: MealRepository
    private var userId: Int = 0

    private lateinit var rgGoal: RadioGroup
    private lateinit var rbBulking: RadioButton
    private lateinit var rbLosing: RadioButton
    private lateinit var rbMaintenance: RadioButton
    private lateinit var etBudget: EditText
    private lateinit var etDiet: EditText
    private lateinit var etDescription: EditText

    override fun onCreateView(
        i: LayoutInflater, c: ViewGroup?, s: Bundle?
    ): View? {
        return i.inflate(R.layout.fragment_setup, c, false)
    }

    override fun onViewCreated(view: View, s: Bundle?) {
        super.onViewCreated(view, s)

        rgGoal = view.findViewById(R.id.rgGoal)
        rbBulking = view.findViewById(R.id.rbBulking)
        rbLosing = view.findViewById(R.id.rbLosing)
        rbMaintenance = view.findViewById(R.id.rbMaintenance)
        etBudget = view.findViewById(R.id.etBudget)
        etDiet = view.findViewById(R.id.etDiet)
        etDescription = view.findViewById(R.id.etDescription)
        val btnSaveProfile = view.findViewById<Button>(R.id.btnSaveProfile)
        val btnGoToSummary = view.findViewById<Button>(R.id.btnGoToSummary)

        userId = requireContext()
            .getSharedPreferences("mealplanner", 0)
            .getInt("userId", 0)

        val db = AppDatabase.get(requireContext())
        repo = MealRepository(db.userDao(), db.mealDao(), db.clientProfileDao())

        // Load existing profile if it exists, and adjust button text accordingly
        lifecycleScope.launch {
            val existing = repo.profileFor(userId)
            if (existing != null) {
                prefill(existing)
                // User already has a profile, so this is an "Edit" screen
                btnGoToSummary.text = "Cancel"
            } else {
                // First-time setup
                btnGoToSummary.text = "Skip for Now"
            }
        }

        // SAVE -> Saves to DB and goes to Profile Page
        btnSaveProfile.setOnClickListener {
            saveProfile {
                findNavController().navigate(R.id.action_setup_to_summary)
            }
        }

        // CANCEL / SKIP -> Goes to Profile Page WITHOUT saving unsaved changes
        btnGoToSummary.setOnClickListener {
            findNavController().navigate(R.id.action_setup_to_summary)
        }
    }

    private fun prefill(p: ClientProfileEntity) {
        etBudget.setText(p.budgetPerDay.toString())
        etDiet.setText(p.dietaryPreference)
        etDescription.setText(p.description)
        val id = when (p.healthGoal) {
            "bulking" -> rbBulking.id
            "losing"  -> rbLosing.id
            else      -> rbMaintenance.id
        }
        rgGoal.check(id)
    }

    private fun saveProfile(onSuccess: () -> Unit) {
        val goal = when (rgGoal.checkedRadioButtonId) {
            rbBulking.id -> "bulking"
            rbLosing.id  -> "losing"
            else         -> "maintenance"
        }
        val budget = etBudget.text.toString().toDoubleOrNull()
        if (budget == null || budget <= 0) {
            Toast.makeText(requireContext(), "Enter a positive budget", Toast.LENGTH_SHORT).show()
            return
        }
        val diet = etDiet.text.toString().ifBlank { "none" }
        val desc = etDescription.text.toString()

        lifecycleScope.launch {
            repo.saveProfile(
                ClientProfileEntity(
                    userId = userId,
                    budgetPerDay = budget,
                    healthGoal = goal,
                    dietaryPreference = diet,
                    description = desc
                )
            )
            Toast.makeText(requireContext(), "Profile Saved!", Toast.LENGTH_SHORT).show()
            onSuccess()
        }
    }
}