package com.example.smartmealplanner.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/** Creates the SQLite database file and its two tables: foods and users (admins + clients). */
class DbHelper(context: Context) : SQLiteOpenHelper(context, "smartmealplanner.db", null, 2) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE foods (
                id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, foodGroup TEXT NOT NULL,
                price INTEGER NOT NULL, calories INTEGER NOT NULL, protein INTEGER NOT NULL,
                vegan INTEGER NOT NULL, vegetarian INTEGER NOT NULL, halal INTEGER NOT NULL, requires TEXT)"""
        )
        db.execSQL(
            """CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL UNIQUE, email TEXT NOT NULL UNIQUE,
                passwordHash TEXT NOT NULL, role TEXT NOT NULL, weeklyBudget INTEGER NOT NULL DEFAULT 1000,
                goal TEXT NOT NULL DEFAULT 'MAINTENANCE', preferences TEXT NOT NULL DEFAULT '',
                appliances TEXT NOT NULL DEFAULT 'STOVE', servings INTEGER NOT NULL DEFAULT 1)"""
        )
    }

    // Version 2 added "username". During development we simply rebuild the tables.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS foods"); db.execSQL("DROP TABLE IF EXISTS users"); onCreate(db)
    }
}

