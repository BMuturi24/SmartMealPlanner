package com.example.smartmealplanner.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smartmealplanner.data.model.ClientProfileEntity
import com.example.smartmealplanner.data.model.Meal
import com.example.smartmealplanner.data.model.User

@Database(
    entities = [User::class, Meal::class, ClientProfileEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun mealDao(): MealDao
    abstract fun clientProfileDao(): ClientProfileDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "meal_planner.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
    }
}