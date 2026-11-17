package com.example.smartmealplanner.data.repository

import com.example.smartmealplanner.data.local.ClientProfileDao
import com.example.smartmealplanner.data.local.MealDao
import com.example.smartmealplanner.data.local.UserDao
import com.example.smartmealplanner.data.model.ClientProfileEntity
import com.example.smartmealplanner.data.model.Meal
import com.example.smartmealplanner.data.model.User

class MealRepository(
    private val userDao: UserDao,
    private val mealDao: MealDao,
    private val profileDao: ClientProfileDao
) {
    suspend fun register(user: User) = userDao.register(user)
    suspend fun login(id: String): User? = userDao.findByLogin(id)

    suspend fun addMeal(meal: Meal) = mealDao.insert(meal)
    suspend fun allMeals(userId: Int) = mealDao.mealsFor(userId)

    suspend fun seedIfEmpty() {
        if (mealDao.count() > 0) return
        mealDao.insertAll(
            listOf(
                Meal(name = "Ugali + Beans + Egg",         calories = 800, costKes = 120, goal = "bulking"),
                Meal(name = "Chapati + Ndengu",            calories = 750, costKes = 100, goal = "bulking"),
                Meal(name = "Rice + Beans + Greens",       calories = 600, costKes = 90,  goal = "maintenance"),
                Meal(name = "Chapati + Cabbage + Egg",     calories = 620, costKes = 100, goal = "maintenance"),
                Meal(name = "Githeri (small)",             calories = 400, costKes = 60,  goal = "losing"),
                Meal(name = "Sukuma Wiki + Ugali (small)", calories = 450, costKes = 70,  goal = "losing"),
                Meal(name = "Mandazi + Tea",               calories = 300, costKes = 40,  goal = "losing"),
                Meal(name = "Smokies + Bread",             calories = 350, costKes = 50,  goal = "losing")
            )
        )
    }

    suspend fun allClients(): List<User> = userDao.getAllClients()

    suspend fun adminAddClient(username: String, email: String, password: String): Long {
        require(username.isNotBlank()) { "Username required" }
        require(email.contains("@"))    { "Valid email required" }
        require(password.length >= 4)   { "Password must be at least 4 chars" }
        return userDao.insert(
            User(username = username, email = email,
                passwordHash = password, role = "CLIENT")
        )
    }

    suspend fun adminDeleteClient(userId: Int) = userDao.deleteClientById(userId)

    suspend fun saveProfile(profile: ClientProfileEntity) = profileDao.upsert(profile)
    suspend fun profileFor(userId: Int): ClientProfileEntity? = profileDao.forUser(userId)
    suspend fun allProfiles(): List<ClientProfileEntity> = profileDao.all()
}