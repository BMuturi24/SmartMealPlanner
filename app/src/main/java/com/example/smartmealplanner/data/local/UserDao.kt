package com.example.smartmealplanner.data.local

import androidx.room.*
import com.example.smartmealplanner.data.model.User

@Dao
interface UserDao {

    @Insert
    suspend fun register(user: User): Long

    @Insert
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM users WHERE (username = :id OR email = :id) LIMIT 1")
    suspend fun findByLogin(id: String): User?

    @Query("SELECT * FROM users WHERE role = 'CLIENT' ORDER BY username ASC")
    suspend fun getAllClients(): List<User>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun findById(userId: Int): User?

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM users WHERE id = :userId AND role = 'CLIENT'")
    suspend fun deleteClientById(userId: Int)
}