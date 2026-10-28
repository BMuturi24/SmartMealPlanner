package com.example.smartmealplanner

data class Client(
    val name: String,
    val phone: String,
    val email: String,
    val budget: String
) {
    fun summary(): String {
        return "Name: $name\nPhone: $phone\nEmail: $email\nMy Budget: $budget"
    }
}