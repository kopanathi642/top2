package com.example.fitx

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

// 1. The Data Model
data class ChatUser(
    val name: String,
    val message: String,
    val time: String,
    val avatarRes: Int // Matches the data in SocialActivity
)

// 2. The Adapter Class
class ChatAdapter(private val userList: List<ChatUser>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // CORRECTED IDs to match 'item_chat_user.xml'
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvMessage: TextView = view.findViewById(R.id.tvMessage)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val imgProfile: ImageView = view.findViewById(R.id.imgAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        // CORRECTED LAYOUT: using 'item_chat_user'
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_user, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val user = userList[position]

        // --- ASSIGN DATA ---
        holder.tvName.text = user.name
        holder.tvMessage.text = user.message
        holder.tvTime.text = user.time
        holder.imgProfile.setImageResource(user.avatarRes)

        // --- NAVIGATION LOGIC (Added) ---
        holder.itemView.setOnClickListener {
            // This handles the click to "Navigate" to a chat
            Toast.makeText(holder.itemView.context, "Opening chat with ${user.name}...", Toast.LENGTH_SHORT).show()

            // TODO: In the future, you will uncomment this to open the real chat screen:
            // val intent = Intent(holder.itemView.context, ChatDetailActivity::class.java)
            // intent.putExtra("USER_NAME", user.name)
            // holder.itemView.context.startActivity(intent)
        }

        // --- STYLING LOGIC (Online/Unread) ---
        // I adapted your logic to work with the standard IDs
        if (position <= 1) {
            // Simulating "Online" / "New Message" style
            holder.tvTime.setTextColor(Color.parseColor("#B4F656")) // Green text for time
            holder.tvName.setTextColor(Color.BLACK)                 // Dark text for name
            // Note: Since item_chat_user.xml might not have 'tvUnreadCount', we skip it to prevent crashes
        } else {
            // Standard style
            holder.tvTime.setTextColor(Color.parseColor("#666666")) // Grey text
            holder.tvName.setTextColor(Color.DKGRAY)
        }
    }

    override fun getItemCount() = userList.size
}