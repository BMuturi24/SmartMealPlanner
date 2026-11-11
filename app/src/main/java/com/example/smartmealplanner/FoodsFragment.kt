package com.example.smartmealplanner

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smartmealplanner.databinding.FragmentFoodsBinding
import com.example.smartmealplanner.model.*

/** ADMIN: add and delete rows of the food database. */
class FoodsFragment : Fragment() {
    private var _binding: FragmentFoodsBinding? = null
    private val binding get() = _binding!!
    private val admin get() = Session.account as Administrator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFoodsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.foodGroupChips.fill(FoodGroup.values().toList(), { it.name.pretty() }, setOf(FoodGroup.CARB))
        binding.addFoodButton.setOnClickListener { addFood() }
        refresh()
    }

    private fun addFood() {
        val name = binding.foodName.text.toString().trim()
        val group = binding.foodGroupChips.checkedItems<FoodGroup>().firstOrNull()
        if (name.isEmpty() || group == null) {
            Toast.makeText(requireContext(), "Enter a name and choose a food group", Toast.LENGTH_SHORT).show(); return
        }
        val vegan = binding.chipVegan.isChecked
        admin.addFood(Session.repo, Food(
            name = name, foodGroup = group,
            pricePerServing = binding.foodPrice.text.toString().toIntOrNull() ?: 0,
            calories = binding.foodKcal.text.toString().toIntOrNull() ?: 0,
            proteinG = binding.foodProtein.text.toString().toIntOrNull() ?: 0,
            vegan = vegan, vegetarian = vegan || binding.chipVegetarian.isChecked,
            halal = binding.chipHalal.isChecked, requires = Appliance.STOVE
        ))
        listOf(binding.foodName, binding.foodPrice, binding.foodKcal, binding.foodProtein).forEach { it.text?.clear() }
        refresh()
    }

    private fun refresh() {
        val foods = Session.repo.getFoods()
        binding.foodsTitle.text = "Food database (${foods.size})"
        binding.foodsContainer.removeAllViews()
        foods.forEach { f ->
            val row = LinearLayout(requireContext()).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
            row.addView(TextView(requireContext()).apply {
                text = "${f.name}\n${f.foodGroup.name.pretty()} • KES ${f.pricePerServing} • ${f.calories} kcal • ${f.proteinG} g"
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            })
            row.addView(Button(requireContext()).apply {
                text = "Delete"
                setOnClickListener { admin.removeFood(Session.repo, f); refresh() }
            })
            binding.foodsContainer.addView(row)
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}