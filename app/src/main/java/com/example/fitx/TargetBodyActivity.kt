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

class TargetBodyActivity : AppCompatActivity() {

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

    // Motivational Quotes for each level
    private val bodyQuotes = listOf(
        "The Elite Look: Maximum definition and discipline.",
        "The Athletic Look: Powerful, toned, and ready for anything.",
        "The Fit Lifestyle: Balanced, healthy, and sustainable.",
        "The Healthy Start: More energy and confidence every day.",
        "The Foundation: Your journey to a new you begins here."
    )

    // Variable to hold the gender
    private var userGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.target_body)

        // 1. Retrieve Gender from the previous activity
        userGender = intent.getStringExtra("USER_GENDER")

        val viewPager = findViewById<ViewPager2>(R.id.target_body_viewpager)
        val slider = findViewById<Slider>(R.id.body_type_slider)
        val fatValue = findViewById<TextView>(R.id.body_fat_value)
        val quoteText = findViewById<TextView>(R.id.body_quote_text)

        val backButton = findViewById<MaterialButton>(R.id.back_button)
        val nextButton = findViewById<MaterialButton>(R.id.next_button)

        // Adapter for ViewPager2
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
        quoteText.text = bodyQuotes[0]

        // Slider → ViewPager sync
        slider.addOnChangeListener { _, value, _ ->
            val idx = value.toInt().coerceIn(0, bodyImages.size - 1)
            if (viewPager.currentItem != idx) viewPager.currentItem = idx

            // Update Text and Quote
            fatValue.text = fatPercentages[idx]
            quoteText.text = bodyQuotes[idx]
        }

        // ViewPager → Slider sync
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (slider.value.toInt() != position) slider.value = position.toFloat()

                // Update Text and Quote
                fatValue.text = fatPercentages[position]
                quoteText.text = bodyQuotes[position]
            }
        })

        // Back button
        backButton.setOnClickListener { onBackPressed() }

        // 2. UPDATED Navigation Logic
        nextButton.setOnClickListener {
            if (userGender == "Female") {
                // Go to Female Specific Focus Area
                val intent = Intent(this, FemaleFocusAreaActivity::class.java)
                intent.putExtra("USER_GENDER", userGender)
                startActivity(intent)
            } else {
                // Go to Standard (Male) Focus Area
                val intent = Intent(this, FocusAreaActivity::class.java)
                intent.putExtra("USER_GENDER", userGender)
                startActivity(intent)
            }
        }
    }
}