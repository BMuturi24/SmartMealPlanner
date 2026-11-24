package com.example.smartmealplanner.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.smartmealplanner.data.model.Meal

@Dao
interface MealDao {

    // Insert a single new meal (used by Admin).
    @Insert
    suspend fun insert(meal: Meal): Long

    // Insert a list of meals (used for seeding the database).
    @Insert
    suspend fun insertAll(meals: List<Meal>)

    // Count how many meals exist (used to check if seeding is necessary).
    @Query("SELECT COUNT(*) FROM meals")
    suspend fun count(): Int

    // LiveData ensures the UI updates automatically if a new meal is added while the screen is open.
    @Query("SELECT * FROM meals WHERE ownerId = 0 OR ownerId = :userId")
    fun observeAll(userId: Int): LiveData<List<Meal>>

    // Fetches static data for the MealViewModel to filter.
    // ownerId = 0 means Admin meals. ownerId = :userId means the user's personal meals.
    @Query("SELECT * FROM meals WHERE ownerId = 0 OR ownerId = :userId")
    suspend fun mealsFor(userId: Int): List<Meal>

    // Delete a meal.
    @Delete
    suspend fun delete(meal: Meal)
}