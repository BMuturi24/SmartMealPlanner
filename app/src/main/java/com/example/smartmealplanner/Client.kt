package com.example.smartmealplanner

//Serializable is built into Java/Kotlin: no plugin needed
// ": Serializable" means "a Tenant can be packed up and sent to another screen

import java.io.Serializable
data class Client(
    val name: String,
    val phone: String,
    val email: String,
    val budget: String
) {
    fun summary(): String {
        return "Name: $name\nPhone: $phone\nEmail: $email\nMy Budget: KES $budget"
    }
}