package com.example.smartmealplanner.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smartmealplanner.data.model.ClientProfileEntity
import com.example.smartmealplanner.data.model.Meal
import com.example.smartmealplanner.data.model.User

// The @Database annotation tells Room to generate the database tables
// based on the entities provided. version = 2 because we added ClientProfileEntity.
@Database(
    entities = [User::class, Meal::class, ClientProfileEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Abstract functions that expose the DAOs to the rest of the app
    abstract fun userDao(): UserDao
    abstract fun mealDao(): MealDao
    abstract fun clientProfileDao(): ClientProfileDao

    companion object {
        // @Volatile ensures that this variable is always read from the main memory,
        // preventing thread caching issues.
        @Volatile private var INSTANCE: AppDatabase? = null

        // The Singleton pattern: We only ever want ONE instance of the database open.
        // If two screens try to access the database at once and create two connections, the app crashes.
        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "meal_planner.db"
                )
                    // fallbackToDestructiveMigration wipes the database if the version increases
                    // instead of crashing the app. Perfect for development.
                    .fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
    }
}