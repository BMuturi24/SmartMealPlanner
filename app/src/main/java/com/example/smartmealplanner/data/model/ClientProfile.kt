package com.example.smartmealplanner.data.model

class ClientProfile(
    private val clientId: Int,
    private var username: String,
    private var budgetPerDay: Double,
    private var dietaryPreference: String,
    private var healthGoal: String,
    private var description: String = ""
) {
    fun getClientId(): Int = clientId
    fun getUsername(): String = username
    fun getBudget(): Double = budgetPerDay
    fun getDietaryPreference(): String = dietaryPreference
    fun getHealthGoal(): String = healthGoal
    fun getDescription(): String = description

    fun setBudget(newBudget: Double) {
        require(newBudget > 0) { "Budget must be positive" }
        budgetPerDay = newBudget
    }

    fun setHealthGoal(goal: String) {
        require(goal in listOf("bulking", "losing", "maintenance")) {
            "Invalid health goal"
        }
        healthGoal = goal
    }

    fun setDietaryPreference(pref: String) {
        dietaryPreference = pref.ifBlank { "none" }
    }

    fun setDescription(desc: String) {
        description = desc
    }
}