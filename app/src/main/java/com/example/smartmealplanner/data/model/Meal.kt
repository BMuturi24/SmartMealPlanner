package com.example.smartmealplanner.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val calories: Int,
    val costKes: Int,
    val goal: String,      // which health goal it fits
    val ownerId: Int = 0   // 0 = global meal, >0 = user-specific meal
)