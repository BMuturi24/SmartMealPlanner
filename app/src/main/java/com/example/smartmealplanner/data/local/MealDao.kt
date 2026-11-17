package com.example.smartmealplanner.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.smartmealplanner.data.model.Meal

@Dao
interface MealDao {

    @Insert
    suspend fun insert(meal: Meal): Long

    @Insert
    suspend fun insertAll(meals: List<Meal>)

    @Query("SELECT COUNT(*) FROM meals")
    suspend fun count(): Int

    @Query("SELECT * FROM meals WHERE ownerId = 0 OR ownerId = :userId")
    fun observeAll(userId: Int): LiveData<List<Meal>>

    @Query("SELECT * FROM meals WHERE ownerId = 0 OR ownerId = :userId")
    suspend fun mealsFor(userId: Int): List<Meal>

    @Delete
    suspend fun delete(meal: Meal)
}