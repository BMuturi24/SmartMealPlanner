package com.example.smartmealplanner.data.model

// This is a pure Domain Class used to demonstrate Encapsulation (OOP).
// It is NOT a Room Entity. It protects the data and validates it before saving to the database.
class ClientProfile(
    private val clientId: Int,
    private var username: String,
    private var budgetPerDay: Double,
    private var dietaryPreference: String,
    private var healthGoal: String,
    private var description: String = ""
) {
    // Getters (public) to allow read access.
    fun getClientId(): Int = clientId
    fun getUsername(): String = username
    fun getBudget(): Double = budgetPerDay
    fun getDietaryPreference(): String = dietaryPreference
    fun getHealthGoal(): String = healthGoal
    fun getDescription(): String = description

    // Setters (with validation) to control how data is changed.
    // If a negative number is passed, the app throws an error and blocks the change.
    fun setBudget(newBudget: Double) {
        require(newBudget > 0) { "Budget must be positive" }
        budgetPerDay = newBudget
    }

    // Ensures users can only select one of the 3 valid goals.
    fun setHealthGoal(goal: String) {
        require(goal in listOf("bulking", "losing", "maintenance")) {
            "Invalid health goal"
        }
        healthGoal = goal
    }

    // If the diet field is left blank, default to "none".
    fun setDietaryPreference(pref: String) {
        dietaryPreference = pref.ifBlank { "none" }
    }

    fun setDescription(desc: String) {
        description = desc
    }
}