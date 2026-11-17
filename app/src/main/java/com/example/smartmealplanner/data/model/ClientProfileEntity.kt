package com.example.smartmealplanner.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "client_profiles",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
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