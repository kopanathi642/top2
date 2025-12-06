package com.example.fitx

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.switchmaterial.SwitchMaterial

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile) // Ensure XML is named activity_profile.xml

        // Initialize Notification Channel to ensure notifications work
        NotificationHelper.createNotificationChannel(this)

        setupUserInfo()
        setupButtons()
        setupBottomNavigation()
    }

    private fun setupUserInfo() {
        val tvName = findViewById<TextView>(R.id.tvUserName)
        val tvEmail = findViewById<TextView>(R.id.tvUserEmail)

        // Optional: Load saved gender if you want to display it
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val savedGender = sharedPref.getString("SAVED_GENDER", "Male")
    }

    private fun setupButtons() {
        // --- NEW: Handle Notification Bell Click ---
        // Using safe call (?.) in case the ID is missing in the profile layout
        findViewById<ImageView>(R.id.notification_button)?.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        // 1. Edit Profile Button
        findViewById<MaterialButton>(R.id.btnEditProfile).setOnClickListener {
            Toast.makeText(this, "Edit Profile clicked", Toast.LENGTH_SHORT).show()
        }

        // 2. Notification Switch with Permission Logic
        val notifSwitch = findViewById<SwitchMaterial>(R.id.swNotifications)
        notifSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Check permissions for Android 13+ (TIRAMISU)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
                    } else {
                        NotificationHelper.showNotification(this, "Notifications Enabled", "You will now receive daily workout reminders!")
                    }
                } else {
                    // Android 12 or lower
                    NotificationHelper.showNotification(this, "Notifications Enabled", "You will now receive daily workout reminders!")
                }
            } else {
                Toast.makeText(this, "Notifications Disabled", Toast.LENGTH_SHORT).show()
            }
        }

        // 3. Privacy & Security
        findViewById<LinearLayout>(R.id.optPrivacy).setOnClickListener {
            Toast.makeText(this, "Opening Privacy Settings...", Toast.LENGTH_SHORT).show()
        }

        // 4. Logout
        findViewById<LinearLayout>(R.id.optLogout).setOnClickListener {
            performLogout()
        }

        // 5. FAB (Floating Action Button)
        findViewById<FloatingActionButton>(R.id.fabEdit).setOnClickListener {
            Toast.makeText(this, "Quick Action", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performLogout() {
        Toast.makeText(this, "Logging Out...", Toast.LENGTH_SHORT).show()

        // Redirect to Login Screen and clear stack
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    // --- NAVIGATION LOGIC ---
    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)

        // Highlight "Profile"
        bottomNav.selectedItemId = R.id.navigation_profile

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                // 1. HOME
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish() // Close Profile
                    true
                }

                // 2. TRAINING
                R.id.navigation_training -> {
                    startActivity(Intent(this, TrainingActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish() // Close Profile
                    true
                }

                // 3. COMMUNITY
                R.id.navigation_community -> {
                    startActivity(Intent(this, SocialActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish() // Close Profile
                    true
                }

                // 4. PROFILE (Stay here)
                R.id.navigation_profile -> true

                else -> false
            }
        }
    }
}