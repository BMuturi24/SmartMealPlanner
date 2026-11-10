package com.example.smartmealplanner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.smartmealplanner.databinding.ActivityClientHomeBinding
import com.example.smartmealplanner.model.Client

/** What a CLIENT sees after login: Welcome header + Setup / Plan / Groceries tabs. */
class ClientHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClientHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Session.init(this)
        // Session is in memory only; if the app process was killed, go back to login.
        if (Session.account !is Client) { goToLogin(); return }

        binding = ActivityClientHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.welcomeText.text = "Welcome ${Session.loginName}"

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_setup -> show(ClientSetupFragment())
                R.id.nav_plan -> show(MealPlanFragment())
                R.id.nav_groceries -> show(GroceryFragment())
                R.id.nav_logout -> { Session.logout(); goToLogin() }
            }
            true
        }
        if (savedInstanceState == null) binding.bottomNav.selectedItemId = R.id.nav_setup
    }

    fun selectTab(itemId: Int) { binding.bottomNav.selectedItemId = itemId }

    private fun show(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        finish()
    }
}