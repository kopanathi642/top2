package com.example.fitx

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider

class CurrentBodyActivity : AppCompatActivity() {

    private val bodyImages = listOf(
        R.drawable.body_full_1,
        R.drawable.body_full_2,
        R.drawable.body_full_3,
        R.drawable.body_full_4,
        R.drawable.body_full_5
    )

    private val fatPercentages = listOf(
        "12-15%",
        "16-20%",
        "21-25%",
        "26-35%",
        "36-45%"
    )

    // Quotes for Current Body
    private val currentBodyQuotes = listOf(
        "Lean Build: A great foundation for adding muscle.",
        "Athletic Build: You are already ahead of the game.",
        "Average Build: The perfect canvas for a transformation.",
        "Solid Build: Let's turn this potential into power.",
        "Heavy Build: The start of your most powerful journey."
    )

    // Variable to hold the gender passed from GoalSelection
    private var userGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.current_body)

        // 1. CRITICAL: Retrieve Gender from previous activity
        userGender = intent.getStringExtra("USER_GENDER")

        val viewPager = findViewById<ViewPager2>(R.id.body_image_viewpager)
        val slider = findViewById<Slider>(R.id.body_type_slider)
        val fatValue = findViewById<TextView>(R.id.body_fat_value)
        val quoteText = findViewById<TextView>(R.id.body_quote_text)

        val backButton = findViewById<MaterialButton>(R.id.back_button)
        val nextButton = findViewById<MaterialButton>(R.id.next_button)

        // ViewPager Adapter
        viewPager.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val imageView = ImageView(parent.context)
                imageView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                return object : RecyclerView.ViewHolder(imageView) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                (holder.itemView as ImageView).setImageResource(bodyImages[position])
            }

            override fun getItemCount(): Int = bodyImages.size
        }

        // Slider setup
        slider.valueFrom = 0f
        slider.valueTo = (bodyImages.size - 1).toFloat()
        slider.stepSize = 1f
        slider.value = 0f

        // Set Initial Values
        fatValue.text = fatPercentages[0]
        quoteText.text = currentBodyQuotes[0]

        // Sync Slider → ViewPager
        slider.addOnChangeListener { _, value, _ ->
            val index = value.toInt().coerceIn(0, bodyImages.size - 1)
            if (viewPager.currentItem != index) viewPager.currentItem = index

            // Update Text and Quote
            fatValue.text = fatPercentages[index]
            quoteText.text = currentBodyQuotes[index]
        }

        // Sync ViewPager → Slider
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (slider.value.toInt() != position) slider.value = position.toFloat()

                // Update Text and Quote
                fatValue.text = fatPercentages[position]
                quoteText.text = currentBodyQuotes[position]
            }
        })

        // Back button
        backButton.setOnClickListener { onBackPressed() }

        // 2. CRITICAL: Pass Gender to the next activity (TargetBodyActivity)
        nextButton.setOnClickListener {
            val intent = Intent(this, TargetBodyActivity::class.java)
            intent.putExtra("USER_GENDER", userGender) // Pass it forward
            startActivity(intent)
        }
    }
}