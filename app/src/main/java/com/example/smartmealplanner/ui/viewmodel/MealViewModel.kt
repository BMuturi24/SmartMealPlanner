package com.example.smartmealplanner.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmealplanner.data.model.ClientProfile
import com.example.smartmealplanner.data.model.Meal
import com.example.smartmealplanner.data.repository.MealRepository
import kotlinx.coroutines.launch

class MealViewModel(private val repo: MealRepository) : ViewModel() {

    private val _recommendations = MutableLiveData<List<Meal>>(emptyList())
    val recommendations: LiveData<List<Meal>> = _recommendations

    private val _profile = MutableLiveData<ClientProfile?>()
    val profile: LiveData<ClientProfile?> = _profile

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    // Fetches the most up-to-date profile from the database and generates the plan
    fun loadProfile(userId: Int) {
        viewModelScope.launch {
            val e = repo.profileFor(userId)
            _profile.value = e?.let {
                ClientProfile(
                    clientId = it.userId,
                    username = "",
                    budgetPerDay = it.budgetPerDay,
                    dietaryPreference = it.dietaryPreference,
                    healthGoal = it.healthGoal,
                    description = it.description
                )
            }
            generatePlanFromProfile()
        }
    }

    fun generatePlanFromProfile() {
        val p = _profile.value ?: return
        viewModelScope.launch {
            val all = repo.allMeals(p.getClientId())
            val filtered = all.filter {
                it.goal == p.getHealthGoal() && it.costKes <= p.getBudget()
            }
            // If strict filtering returns nothing, just show what fits the budget
            _recommendations.value =
                if (filtered.isNotEmpty()) filtered
                else all.filter { it.costKes <= p.getBudget() }
        }
    }

    fun consumeMessage() { _message.value = null }
}