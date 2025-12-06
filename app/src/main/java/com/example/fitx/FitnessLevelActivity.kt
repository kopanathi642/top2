package com.example.fitx

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.button.MaterialButton

class FitnessLevelActivity : AppCompatActivity() {

    private var selectedLevel: String? = null

    // Variable to store the gender
    private var userGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fitness_level) // Make sure XML is named fitness_level.xml

        // 1. Retrieve Gender from previous activity (MotivationsActivity)
        userGender = intent.getStringExtra("USER_GENDER")

        // Cards
        val cardBeginner = findViewById<MaterialCardView>(R.id.cardBeginner)
        val cardIntermediate = findViewById<MaterialCardView>(R.id.cardIntermediate)
        val cardAdvanced = findViewById<MaterialCardView>(R.id.cardAdvanced)

        // Buttons
        val btnBack = findViewById<MaterialButton>(R.id.back_button)
        val btnNext = findViewById<MaterialButton>(R.id.next_button)

        // Put all cards in list for easy loop
        val cards = listOf(cardBeginner, cardIntermediate, cardAdvanced)

        // Click events for cards
        cards.forEach { card ->
            card.setOnClickListener {
                selectCard(card, cards)
            }
        }

        // Back Button
        btnBack.setOnClickListener {
            finish()
        }

        // Next Button
        btnNext.setOnClickListener {
            if (selectedLevel == null) {
                Toast.makeText(this, "Please select your fitness level", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, HomeActivity::class.java)

                // Pass Fitness Level
                intent.putExtra("FITNESS_LEVEL", selectedLevel)

                // 2. Pass Gender to HomeActivity (Critical for character image)
                intent.putExtra("USER_GENDER", userGender)

                startActivity(intent)
            }
        }
    }

    private fun selectCard(selectedCard: MaterialCardView, allCards: List<MaterialCardView>) {

        // Reset all cards
        allCards.forEach { card ->
            card.setCardBackgroundColor(Color.parseColor("#1C1C1E"))
            card.strokeColor = Color.parseColor("#333333")
            card.strokeWidth = 1

            val layout = card.getChildAt(0) as ViewGroup
            val title = layout.getChildAt(0) as TextView
            val subtitle = layout.getChildAt(1) as TextView
            title.setTextColor(Color.WHITE)
            subtitle.setTextColor(Color.parseColor("#888888"))
        }

        // Highlight selected card
        selectedCard.setCardBackgroundColor(Color.parseColor("#B4F656"))
        selectedCard.strokeColor = Color.parseColor("#B4F656")
        selectedCard.strokeWidth = 4

        // Change text color inside selected card
        val container = selectedCard.getChildAt(0) as ViewGroup
        val title = container.getChildAt(0) as TextView
        val subtitle = container.getChildAt(1) as TextView

        title.setTextColor(Color.BLACK)
        subtitle.setTextColor(Color.BLACK)

        // Save selected level
        selectedLevel = title.text.toString()
    }
}