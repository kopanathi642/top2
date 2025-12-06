package com.example.fitx

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView // Added Import
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class SocialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox_modern) // Ensure layout is named activity_inbox_modern.xml

        setupHeader() // <--- NEW: Initialize Notification Bell logic
        setupFakeData()
        setupTabsLogic()
        setupBottomNavigation()
        setupFab()
    }

    // --- NEW: Handle Notification Button Click ---
    private fun setupHeader() {
        // Use safe call (?.) just in case the ID is missing in this specific XML
        val notifButton = findViewById<ImageView>(R.id.notification_button)

        notifButton?.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupFakeData() {
        // --- PART 1: CHATS ---
        val activeUsers = listOf(
            ChatUser("Sarah", "Online", "", R.drawable.ic_launcher_background),
            ChatUser("Mike", "Online", "", R.drawable.ic_launcher_background),
            ChatUser("Coach", "Online", "", R.drawable.ic_launcher_background)
        )

        val chatUsers = listOf(
            ChatUser("Alex Johnson", "Did you hit your PR today?", "2m", R.drawable.ic_launcher_background),
            ChatUser("Fitness Group", "Meet at 5 PM!", "14m", R.drawable.ic_launcher_background),
            ChatUser("Yoga Instructor", "Don't forget to stretch.", "1h", R.drawable.ic_launcher_background)
        )

        // Setup Active Users (Horizontal List)
        val recyclerActive = findViewById<RecyclerView>(R.id.recyclerActive)
        recyclerActive.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerActive.adapter = ChatAdapter(activeUsers)

        // Setup Recent Chats (Vertical List)
        val recyclerChats = findViewById<RecyclerView>(R.id.recyclerChats)
        recyclerChats.layoutManager = LinearLayoutManager(this)
        recyclerChats.adapter = ChatAdapter(chatUsers)


        // --- PART 2: POSTS ---
        val posts = listOf(
            PostModel(
                "Sarah Fitness",
                "2 hours ago",
                "Just crushed my leg day workout! Hit a new PR on squats. 100kg for 5 reps! 💪 #LegDay",
                "124", "22",
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background
            ),
            PostModel(
                "Mike The Trainer",
                "5 hours ago",
                "Remember: Consistency is key. You don't have to be extreme, just consistent.",
                "85", "10",
                R.drawable.ic_launcher_background,
                null
            ),
            PostModel(
                "Healthy Foodie",
                "1 day ago",
                "Made this amazing avocado toast for breakfast. Recipe in bio! 🥑",
                "200", "45",
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_background
            )
        )

        val recyclerPosts = findViewById<RecyclerView>(R.id.recyclerPosts)
        recyclerPosts.layoutManager = LinearLayoutManager(this)
        recyclerPosts.adapter = PostAdapter(posts)
    }

    private fun setupTabsLogic() {
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val layoutChats = findViewById<LinearLayout>(R.id.layoutChats)
        val recyclerPosts = findViewById<RecyclerView>(R.id.recyclerPosts)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> { // Chats Tab
                        layoutChats.visibility = View.VISIBLE
                        recyclerPosts.visibility = View.GONE
                    }
                    1 -> { // Community Feed Tab
                        layoutChats.visibility = View.GONE
                        recyclerPosts.visibility = View.VISIBLE
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    // --- NAVIGATION LOGIC ---
    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav.selectedItemId = R.id.navigation_community

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish() // Close SocialActivity
                    true
                }
                R.id.navigation_training -> {
                    startActivity(Intent(this, TrainingActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish() // Close SocialActivity
                    true
                }
                R.id.navigation_community -> true // Stay here
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish() // Close SocialActivity
                    true
                }
                else -> false
            }
        }
    }

    private fun setupFab() {
        findViewById<FloatingActionButton>(R.id.fabAction).setOnClickListener {
            Toast.makeText(this, "Create new Message/Post", Toast.LENGTH_SHORT).show()
        }
    }
}