package com.example.fitx

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(private val postList: List<PostModel>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvUserName)
        val tvTime: TextView = view.findViewById(R.id.tvPostTime)
        val tvContent: TextView = view.findViewById(R.id.tvPostContent)
        val imgAvatar: ImageView = view.findViewById(R.id.imgUserAvatar)
        val imgPost: ImageView = view.findViewById(R.id.imgPostImage)
        val tvLikes: TextView = view.findViewById(R.id.tvLikes)       // Added
        val tvComments: TextView = view.findViewById(R.id.tvComments) // Added
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        // Connects to item_community_post.xml
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_community_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]

        holder.tvName.text = post.userName
        holder.tvTime.text = post.time
        holder.tvContent.text = post.content
        holder.imgAvatar.setImageResource(post.avatarRes)

        holder.tvLikes.text = "${post.likes} Likes"
        holder.tvComments.text = "${post.comments} Comments"

        // Logic: Only show the big post image if one exists
        if (post.postImageRes != null) {
            holder.imgPost.visibility = View.VISIBLE
            holder.imgPost.setImageResource(post.postImageRes)
        } else {
            holder.imgPost.visibility = View.GONE
        }
    }

    override fun getItemCount() = postList.size
}