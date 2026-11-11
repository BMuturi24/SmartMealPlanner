package com.example.smartmealplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.example.smartmealplanner.databinding.FragmentMealPlanBinding

/** PLAN tab: budget summary plus the 7-day meal plan. */
class MealPlanFragment : Fragment() {
    private var _binding: FragmentMealPlanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMealPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val plan = Session.plan
        if (plan == null) { binding.emptyText.visibility = View.VISIBLE; return }
        binding.emptyText.visibility = View.GONE
        val box = binding.planContainer
        box.removeAllViews()

        box.addCard {
            addText("${plan.goal.label} • ${plan.people} ${if (plan.people == 1) "person" else "people"}")
            addText("${kes(plan.totalCost)} of ${kes(plan.weeklyBudget.toDouble())}", 20f, bold = true, topDp = 4)
            addView(ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal).apply {
                max = 100
                progress = (plan.totalCost / plan.weeklyBudget * 100).toInt().coerceIn(0, 100)
            })
            addText(if (plan.withinBudget) "Within budget ✔" else "Slightly over budget – try a bigger budget or fewer servings", topDp = 4)
        }

        plan.days.forEach { day ->
            box.addCard {
                addText(day.day, 18f, bold = true)
                day.meals.forEach { m ->
                    addText("${m.slot.name.pretty()}: ${m.title}", topDp = 8)
                    addText("%.0f kcal • %.0f g protein • %s".format(m.calories, m.proteinG, kes(m.cost)), 12f)
                }
                addText("Day total: %.0f kcal".format(day.calories), 12f, bold = true, topDp = 8)
            }
        }
        box.addText("This app gives general food ideas only and is not medical or dietitian advice.", 11f, topDp = 4)
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}