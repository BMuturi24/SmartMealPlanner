package com.example.smartmealplanner.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// This is the actual Room table definition for saving to SQLite.
// It uses a ForeignKey to link each profile to a specific User.
@Entity(
    tableName = "client_profiles",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            // CASCADE means: If the User is deleted, delete their profile automatically.
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ClientProfileEntity(
    @PrimaryKey val userId: Int,
    val budgetPerDay: Double,
    val healthGoal: String,
    val dietaryPreference: String,
    val description: String
)