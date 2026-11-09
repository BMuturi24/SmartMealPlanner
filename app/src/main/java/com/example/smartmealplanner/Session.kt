package com.example.smartmealplanner

import android.content.Context
import com.example.smartmealplanner.data.DbHelper
import com.example.smartmealplanner.data.MealRepository
import com.example.smartmealplanner.model.Account
import com.example.smartmealplanner.model.WeeklyPlan

/** In-memory app state shared by all screens: the database, who is logged in, and the latest plan. */
object Session {
    lateinit var repo: MealRepository
        private set
    var account: Account? = null
    var loginName: String = ""      // what the person typed to log in (username or email)
    var plan: WeeklyPlan? = null

    fun init(context: Context) {
        if (!::repo.isInitialized) {
            repo = MealRepository(DbHelper(context.applicationContext))
            repo.seedIfEmpty()
        }
    }

    fun logout() { account = null; loginName = ""; plan = null }
}