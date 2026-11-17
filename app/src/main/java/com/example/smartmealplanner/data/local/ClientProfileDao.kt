package com.example.smartmealplanner.data.local

import androidx.room.*
import com.example.smartmealplanner.data.model.ClientProfileEntity

@Dao
interface ClientProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: ClientProfileEntity)

    @Query("SELECT * FROM client_profiles WHERE userId = :userId LIMIT 1")
    suspend fun forUser(userId: Int): ClientProfileEntity?

    @Query("SELECT * FROM client_profiles")
    suspend fun all(): List<ClientProfileEntity>

    @Delete
    suspend fun delete(profile: ClientProfileEntity)
}