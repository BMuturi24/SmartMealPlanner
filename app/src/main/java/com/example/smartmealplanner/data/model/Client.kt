package com.example.smartmealplanner.data.model

/** Client-specific fields. The profile itself is encapsulated below. */
data class Client(
    val userId: Int,
    val budgetPerDay: Double,
    val dietaryPreference: String,
    val healthGoal: String
)