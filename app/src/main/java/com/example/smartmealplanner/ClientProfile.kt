package com.example.smartmealplanner.model

/** Read-only copy of a profile, used only for saving to the database / feeding the planner. */
data class ProfileSnapshot(
    val weeklyBudget: Int,
    val goal: HealthGoal,
    val preferences: Set<DietaryPreference>,
    val appliances: Set<Appliance>,
    val servings: Int
)

/**
 * ENCAPSULATION: every field is private. The outside world can read values
 * (through get-only properties that return copies) but can only change them
 * through the validating methods below, so a bad budget or goal can never get in.
 */
class ClientProfile(
    weeklyBudget: Int = 1000,
    goal: HealthGoal = HealthGoal.MAINTENANCE,
    preferences: Set<DietaryPreference> = emptySet(),
    appliances: Set<Appliance> = setOf(Appliance.STOVE),
    servings: Int = 1
) {
    private var _weeklyBudget: Int = validBudget(weeklyBudget)
    private var _goal: HealthGoal = goal
    private var _servings: Int = validServings(servings)
    private val _preferences = mutableSetOf<DietaryPreference>()
    private val _appliances = mutableSetOf<Appliance>()

    init {
        setDietaryPreferences(preferences)
        setAppliances(appliances)
    }

    // ----- controlled READ access (defensive copies, so callers can't mutate our sets) -----
    val weeklyBudget: Int get() = _weeklyBudget
    val goal: HealthGoal get() = _goal
    val servings: Int get() = _servings
    val preferences: Set<DietaryPreference> get() = _preferences.toSet()
    val appliances: Set<Appliance> get() = _appliances.toSet()

    // ----- controlled WRITE access (validated) -----
    fun updateBudget(newBudget: Int) { _weeklyBudget = validBudget(newBudget) }
    fun changeGoal(newGoal: HealthGoal) { _goal = newGoal }
    fun setServings(people: Int) { _servings = validServings(people) }

    fun setDietaryPreferences(prefs: Set<DietaryPreference>) {
        _preferences.clear()
        _preferences.addAll(prefs)
        // business rule: vegan always implies vegetarian
        if (DietaryPreference.VEGAN in _preferences) _preferences.add(DietaryPreference.VEGETARIAN)
    }

    fun setAppliances(items: Set<Appliance>) {
        _appliances.clear()
        _appliances.addAll(items)
    }

    fun snapshot() = ProfileSnapshot(_weeklyBudget, _goal, preferences, appliances, _servings)

    private companion object {
        fun validBudget(b: Int): Int {
            require(b in 100..100_000) { "Weekly budget must be between KES 100 and KES 100,000" }
            return b
        }
        fun validServings(n: Int): Int {
            require(n in 1..10) { "Servings must be between 1 and 10" }
            return n
        }
    }
}