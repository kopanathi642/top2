package com.example.fitx

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class TrainingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        setupHeader() // <--- Initialize Notification Bell Logic
        setupCategories()
        setupWorkouts()
        setupSuggestedVideos()
        setupBottomNavigation()
    }

    // --- NEW: Header Logic (Notification Bell) ---
    private fun setupHeader() {
        val notifButton = findViewById<ImageView>(R.id.notification_button)
        // Use safe call in case ID is missing in other layouts (though we just added it to XML)
        notifButton?.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }
    }

    // --- 1. CATEGORIES LOGIC ---
    private fun setupCategories() {
        val categories = listOf("Yoga", "Cardio", "Strength", "Pilates", "HIIT")
        val recycler = findViewById<RecyclerView>(R.id.recyclerCategories)
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = CategoryAdapter(categories)
    }

    // --- 2. WORKOUTS LOGIC ---
    private fun setupWorkouts() {
        val workouts = listOf(
            WorkoutModel("Morning Yoga Flow", "15 Mins • Beginner"),
            WorkoutModel("Leg Day Destruction", "45 Mins • Advanced"),
            WorkoutModel("Core Blaster", "10 Mins • Intermediate"),
            WorkoutModel("Upper Body Power", "30 Mins • Intermediate")
        )
        val recycler = findViewById<RecyclerView>(R.id.recyclerWorkouts)
        // Ensure this recycler view doesn't capture scroll focus from nested scroll view
        recycler.isNestedScrollingEnabled = false
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = WorkoutAdapter(workouts)
    }

    // --- 3. SUGGESTED VIDEOS LOGIC ---
    private fun setupSuggestedVideos() {
        val videos = listOf(
            WorkoutModel("Warm Up", "5 Min"),
            WorkoutModel("Stretching", "10 Min"),
            WorkoutModel("Abs", "8 Min"),
            WorkoutModel("Cool Down", "5 Min")
        )

        val recycler = findViewById<RecyclerView>(R.id.recyclerSuggestedVideos)
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = VideoAdapter(videos)
    }

    // --- 4. NAVIGATION LOGIC ---
    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav.selectedItemId = R.id.navigation_training

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish() // Close TrainingActivity so Home reloads
                    true
                }
                R.id.navigation_community -> {
                    startActivity(Intent(this, SocialActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.navigation_training -> true // Stay here
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

    // --- INTERNAL ADAPTERS & MODELS ---

    // Data Model (Reused for Workouts and Videos)
    data class WorkoutModel(val name: String, val meta: String)

    // Category Adapter
    class CategoryAdapter(private val list: List<String>) : RecyclerView.Adapter<CategoryAdapter.Holder>() {
        class Holder(v: View) : RecyclerView.ViewHolder(v) {
            val text: TextView = v.findViewById(R.id.tvCategoryName)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
            return Holder(v)
        }
        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.text.text = list[position]
        }
        override fun getItemCount() = list.size
    }

    // Workout Adapter (Vertical List)
    class WorkoutAdapter(private val list: List<WorkoutModel>) : RecyclerView.Adapter<WorkoutAdapter.Holder>() {
        class Holder(v: View) : RecyclerView.ViewHolder(v) {
            val name: TextView = v.findViewById(R.id.tvWorkoutTitle)
            val meta: TextView = v.findViewById(R.id.tvWorkoutLevel)
            val duration: TextView = v.findViewById(R.id.tvDuration)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_workout_pro, parent, false)
            return Holder(v)
        }
        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.name.text = list[position].name
            // Splitting the meta string roughly to fit the new layout
            val metaParts = list[position].meta.split("•")
            if (metaParts.size > 1) {
                holder.duration.text = metaParts[0].trim()
                holder.meta.text = metaParts[1].trim().uppercase()
            } else {
                holder.meta.text = list[position].meta
                holder.duration.text = ""
            }
        }
        override fun getItemCount() = list.size
    }

    // Video Adapter (Horizontal Box List)
    class VideoAdapter(private val list: List<WorkoutModel>) : RecyclerView.Adapter<VideoAdapter.Holder>() {
        class Holder(v: View) : RecyclerView.ViewHolder(v) {
            val title: TextView = v.findViewById(R.id.tvVideoTitle)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_video_card, parent, false)
            return Holder(v)
        }
        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.title.text = list[position].name
        }
        override fun getItemCount() = list.size
    }
}