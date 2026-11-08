package com.example.smartmealplanner

object ClientsRepository {
    val client = mutableListOf<Client>()
    fun addTenant(tenant: Client) {
        client.add(client)
    }
    fun totalBudget(): Double {
        return client.sumOf { it.budget.toDoubleOrNull() ?: 0.0 }
    }
}