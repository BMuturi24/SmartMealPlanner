package com.example.smartmealplanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartmealplanner.databinding.FragmentGroceryBinding

/** GROCERIES tab: the shopping list for the generated plan. */
class GroceryFragment : Fragment() {
    private var _binding: FragmentGroceryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGroceryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val plan = Session.plan
        if (plan == null) { binding.emptyText.visibility = View.VISIBLE; return }
        binding.emptyText.visibility = View.GONE
        val box = binding.groceryContainer
        box.removeAllViews()
        box.addText("Grocery list • ${kes(plan.totalCost)}", 20f, bold = true)
        plan.grocery.forEach { g ->
            box.addCard {
                addText(g.food.name, 16f, bold = true)
                addText("%.1f servings • %s • %s".format(g.servings, g.food.foodGroup.name.lowercase(), kes(g.cost)), 13f)
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}