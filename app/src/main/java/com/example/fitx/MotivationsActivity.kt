package com.example.fitx

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class MotivationsActivity : AppCompatActivity() {

    private val selectedMotivations = mutableSetOf<String>()

    // Variable to hold the gender
    private var userGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Correct layout name
        setContentView(R.layout.activity_motivations)

        // 1. Retrieve Gender from previous activity (MetricsInputActivity)
        userGender = intent.getStringExtra("USER_GENDER")

        // Initialize Cards
        val cardConfident = findViewById<MaterialCardView>(R.id.cardConfident)
        val cardStress = findViewById<MaterialCardView>(R.id.cardStress)
        val cardHealth = findViewById<MaterialCardView>(R.id.cardHealth)
        val cardEnergy = findViewById<MaterialCardView>(R.id.cardEnergy)

        // Buttons
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnNext = findViewById<Button>(R.id.btnNext)

        // Group cards
        val cards = listOf(cardConfident, cardStress, cardHealth, cardEnergy)

        // Card selection handling
        cards.forEach { card ->
            card.setOnClickListener {
                toggleSelection(card)
            }
        }

        // Back button
        btnBack.setOnClickListener {
            finish()
        }

        // Next button
        btnNext.setOnClickListener {
            if (selectedMotivations.isEmpty()) {
                Toast.makeText(this, "Please select at least one motivation", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, FitnessLevelActivity::class.java)
                intent.putStringArrayListExtra("MOTIVATIONS", ArrayList(selectedMotivations))

                // 2. Pass Gender to the next activity
                intent.putExtra("USER_GENDER", userGender)

                startActivity(intent)
            }
        }
    }

    private fun toggleSelection(card: MaterialCardView) {

        val container = card.getChildAt(0) as? ViewGroup
        val textView = container?.getChildAt(0) as? TextView

        if (textView != null) {
            val text = textView.text.toString()

            if (selectedMotivations.contains(text)) {
                // Deselect
                selectedMotivations.remove(text)
                card.setCardBackgroundColor(Color.parseColor("#222222"))
                textView.setTextColor(Color.WHITE)
                card.strokeWidth = 0
            } else {
                // Select
                selectedMotivations.add(text)
                card.setCardBackgroundColor(Color.parseColor("#B4F656"))
                textView.setTextColor(Color.BLACK)
                card.strokeColor = Color.parseColor("#B4F656")
                card.strokeWidth = 4
            }
        }
    }
}