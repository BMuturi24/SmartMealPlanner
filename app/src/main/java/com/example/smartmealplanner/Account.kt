package com.example.smartmealplanner.model

import com.example.smartmealplanner.data.MealRepository
import java.security.MessageDigest

object PasswordHasher {
    // Prototype-level hashing. For production add a per-account salt and use bcrypt/Argon2.
    fun hash(raw: String): String =
        MessageDigest.getInstance("SHA-256").digest(raw.toByteArray())
            .joinToString("") { "%02x".format(it) }
}

/**
 * INHERITANCE: details every person using the app shares (username, email, password) live once
 * in this base class. Client and Administrator inherit them and add only what makes them different.
 */
abstract class Account(
    val id: Long,
    val username: String,
    val email: String,
    private val passwordHash: String
) {
    abstract val role: Role
    fun passwordMatches(raw: String) = PasswordHasher.hash(raw) == passwordHash
    fun storedHash() = passwordHash
}

/** A Client (a student or anyone else) additionally owns a dietary profile and budget. */
class Client(
    id: Long, username: String, email: String, passwordHash: String,
    val profile: ClientProfile
) : Account(id, username, email, passwordHash) {
    override val role = Role.CLIENT
}

/** An Administrator additionally manages the food database and client accounts. */
class Administrator(
    id: Long, username: String, email: String, passwordHash: String
) : Account(id, username, email, passwordHash) {
    override val role = Role.ADMIN

    fun addClient(repo: MealRepository, username: String, email: String, password: String) =
        repo.createAccount(username, email, password, Role.CLIENT)
    fun removeClient(repo: MealRepository, clientId: Long) = repo.deleteClient(clientId)
    fun addFood(repo: MealRepository, food: Food) = repo.addFood(food)
    fun removeFood(repo: MealRepository, food: Food) = repo.deleteFood(food)
}