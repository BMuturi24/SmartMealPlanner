package com.example.smartmealplanner.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Base entity for everyone who logs in.
 * `role` distinguishes Client vs Admin so the DAO can query separately.
 */
@Entity(tableName = "users")
open class User(
    @PrimaryKey(autoGenerate = true) open val id: Int = 0,
    open var username: String,
    open var email: String,
    open var passwordHash: String,
    open var role: String   // "CLIENT" or "ADMIN"
)