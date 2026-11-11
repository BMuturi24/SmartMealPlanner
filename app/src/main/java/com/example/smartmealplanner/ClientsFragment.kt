package com.example.smartmealplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smartmealplanner.databinding.FragmentClientSetupBinding
import com.example.smartmealplanner.model.*
import com.example.smartmealplanner.planner.PlanGenerator
import com.example.smartmealplanner.planner.PlanResult
import com.example.smartmealplanner.planner.SmartMealPlanner

/** SETUP tab: collects servings, budget, goal, diet, appliances, then Generate Plan. */
class ClientSetupFragment : Fragment() {
    private var _binding: FragmentClientSetupBinding? = null
    private val binding get() = _binding!!
    private var servings = 1
    private val planner: PlanGenerator = SmartMealPlanner()   // depends on the abstraction

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentClientSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val client = Session.account as Client
        val p = client.profile                        // read via public getters only
        // After a rotation Android gives us back the Bundle we filled in onSaveInstanceState.
        // If it has values, show the half-edited form; otherwise show the saved profile.
        val saved = savedInstanceState
        servings = saved?.getInt("servings") ?: p.servings
        val goal = saved?.getString("goal")?.let { HealthGoal.valueOf(it) } ?: p.goal
        val prefs = saved?.getStringArrayList("prefs")?.map { DietaryPreference.valueOf(it) }?.toSet() ?: p.preferences
        val apps = saved?.getStringArrayList("apps")?.map { Appliance.valueOf(it) }?.toSet() ?: p.appliances

        binding.servingsText.text = servings.toString()
        binding.budgetInput.setText(saved?.getString("budget") ?: p.weeklyBudget.toString())
        binding.goalGroup.fill(HealthGoal.values().toList(), { it.label }, setOf(goal))
        binding.prefGroup.fill(DietaryPreference.values().toList(), { it.name.pretty() }, prefs)
        binding.applianceGroup.fill(Appliance.values().toList(), { it.name.pretty() }, apps)

        binding.minusButton.setOnClickListener { if (servings > 1) { servings--; binding.servingsText.text = servings.toString() } }
        binding.plusButton.setOnClickListener { if (servings < 10) { servings++; binding.servingsText.text = servings.toString() } }
        binding.generateButton.setOnClickListener { generate(client) }
        binding.savePrefsButton.setOnClickListener {
            if (savePreferences(client)) {
                Toast.makeText(requireContext(), "Preferences saved. Tap Generate Plan to update your meals.", Toast.LENGTH_LONG).show()
            }
        }
    }

    /** Copies the form into the profile through its validating methods, then saves to the database. */
    private fun savePreferences(client: Client): Boolean {
        val goal = binding.goalGroup.checkedItems<HealthGoal>().firstOrNull() ?: HealthGoal.MAINTENANCE
        try {
            // Every change goes through the profile's validating methods (encapsulation).
            client.profile.updateBudget(binding.budgetInput.text.toString().toIntOrNull() ?: 0)
            client.profile.setServings(servings)
            client.profile.changeGoal(goal)
            client.profile.setDietaryPreferences(binding.prefGroup.checkedItems())
            client.profile.setAppliances(binding.applianceGroup.checkedItems())
        } catch (e: IllegalArgumentException) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            return false
        }
        Session.repo.saveClient(client)
        return true
    }

    private fun generate(client: Client) {
        if (!savePreferences(client)) return
        when (val result = planner.generatePlan(Session.repo.getFoods(), client.profile)) {
            is PlanResult.Success -> {
                Session.plan = result.plan
                (requireActivity() as ClientHomeActivity).selectTab(R.id.nav_plan)
            }
            is PlanResult.Failure -> Toast.makeText(requireContext(), result.reason, Toast.LENGTH_LONG).show()
        }
    }

    /** Called just before the screen is destroyed (for example when the phone rotates). */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (_binding == null) return
        outState.putInt("servings", servings)
        outState.putString("budget", binding.budgetInput.text.toString())
        outState.putString("goal", binding.goalGroup.checkedItems<HealthGoal>().firstOrNull()?.name)
        outState.putStringArrayList("prefs", ArrayList(binding.prefGroup.checkedItems<DietaryPreference>().map { it.name }))
        outState.putStringArrayList("apps", ArrayList(binding.applianceGroup.checkedItems<Appliance>().map { it.name }))
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}