package com.example.smartmealplanner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.smartmealplanner.databinding.ActivityMainBinding
import com.example.smartmealplanner.model.Administrator

/** What the ADMINISTRATOR sees after login: a dashboard plus Add client / View clients / Manage foods. */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Session.init(this)
        if (Session.account !is Administrator) { logout(); return }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, DashboardFragment()).commit()
        }
    }

    /** Open a sub-screen; the system Back button returns to the dashboard. */
    fun open(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit()
    }

    fun back() { supportFragmentManager.popBackStack() }

    fun logout() {
        Session.logout()
        startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        finish()
    }
}