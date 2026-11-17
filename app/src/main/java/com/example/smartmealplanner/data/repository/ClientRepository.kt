package com.example.smartmealplanner.data.repository

import com.example.smartmealplanner.data.model.User

/** Thin façade for client-only operations, delegating to MealRepository. */
class ClientRepository(private val repo: MealRepository) {

    suspend fun allClients(): List<User> = repo.allClients()

    suspend fun addClient(username: String, email: String, password: String): Long =
        repo.adminAddClient(username, email, password)

    suspend fun deleteClient(userId: Int) = repo.adminDeleteClient(userId)
}