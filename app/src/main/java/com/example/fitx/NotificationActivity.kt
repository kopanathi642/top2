package com.example.fitx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        // 1. Setup Back Button
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // 2. Setup RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerNotifications)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 3. Fake Data
        val notifications = listOf(
            NotifModel("Time to Workout!", "Don't miss your leg day session.", "Now", R.drawable.ic_launcher_background),
            NotifModel("New Badge Unlocked", "You reached 5-day streak!", "2h ago", R.drawable.ic_launcher_background),
            NotifModel("Friend Request", "Sarah wants to connect.", "5h ago", R.drawable.ic_launcher_background),
            NotifModel("System Update", "New features added to FitX.", "1d ago", R.drawable.ic_launcher_background)
        )

        recyclerView.adapter = NotificationAdapter(notifications)
    }
}

// --- Internal Data Model ---
data class NotifModel(
    val title: String,
    val message: String,
    val time: String,
    val iconRes: Int
)

// --- Internal Adapter ---
class NotificationAdapter(private val list: List<NotifModel>) : RecyclerView.Adapter<NotificationAdapter.Holder>() {
    class Holder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.tvTitle)
        val msg: TextView = v.findViewById(R.id.tvMessage)
        val time: TextView = v.findViewById(R.id.tvTime)
        val icon: ImageView = v.findViewById(R.id.imgIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list[position]
        holder.title.text = item.title
        holder.msg.text = item.message
        holder.time.text = item.time
        holder.icon.setImageResource(item.iconRes)
    }

    override fun getItemCount() = list.size
}