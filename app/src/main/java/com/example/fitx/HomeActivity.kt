package com.example.fitx

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView

// --- EXPLICIT IMPORTS ADDED TO FIX "UNRESOLVED REFERENCE" ---
import com.example.fitx.TrainingActivity
import com.example.fitx.SocialActivity
import com.example.fitx.ProfileActivity
import com.example.fitx.NotificationActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard) // Ensure XML is named 'dashboard.xml'

        // 1. Handle Gender Logic (Save & Load)
        handleGenderPersistence()

        setupDashboardMetrics()
        setupHeaderInteractions()
        setupBottomNavigation()
    }

    private fun handleGenderPersistence() {
        val characterImageView = findViewById<ImageView>(R.id.character_video)

        // Access the phone's local storage
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        // 1. Try to get gender from the Intent (Fresh login/setup)
        var currentGender = intent.getStringExtra("USER_GENDER")

        if (currentGender != null) {
            // If we received a gender, SAVE it for later
            val editor = sharedPref.edit()
            editor.putString("SAVED_GENDER", currentGender)
            editor.apply()
        } else {
            // If Intent is null (Coming back from Training/Profile), LOAD saved gender
            currentGender = sharedPref.getString("SAVED_GENDER", "Male")
        }

        // 2. Set the Image based on the final determined gender
        if (currentGender == "Female") {
            // Ensure R.drawable.your_female_character exists!
            characterImageView.setImageResource(R.drawable.your_female_character)
        } else {
            characterImageView.setImageResource(R.drawable.male_character)
        }
    }

    private fun setupDashboardMetrics() {
        // 1. Steps Card
        findViewById<TextView>(R.id.steps_value).text = "8,240 / 10,000"

        // 2. Calories Card
        findViewById<TextView>(R.id.calories_value).text = "640 kcal"

        // 3. Active Minutes Card
        findViewById<TextView>(R.id.minutes_value).text = "52 / 60 mins"

        // 4. Water Intake Card
        findViewById<TextView>(R.id.water_value).text = "1.5 / 2.5 L"

        // Interaction Example
        findViewById<MaterialCardView>(R.id.steps_card).setOnClickListener {
            Toast.makeText(this, "Opening Steps Details...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupHeaderInteractions() {
        // --- UPDATED: Open Notification Screen ---
        findViewById<ImageView>(R.id.notification_button).setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.streak_icon).setOnClickListener {
            Toast.makeText(this, "You are on a 5-day streak! Keep it up!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)

        // Set the "Home" item as currently selected
        bottomNav.selectedItemId = R.id.navigation_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                // 1. HOME (Stay here)
                R.id.navigation_home -> true

                // 2. TRAINING
                R.id.navigation_training -> {
                    startActivity(Intent(this, TrainingActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish() // Close Home
                    true
                }

                // 3. COMMUNITY
                R.id.navigation_community -> {
                    startActivity(Intent(this, SocialActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }

                // 4. PROFILE
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}