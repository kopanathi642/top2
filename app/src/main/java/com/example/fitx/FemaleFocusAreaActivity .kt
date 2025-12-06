package com.example.fitx

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FemaleFocusAreaActivity : AppCompatActivity() {

    private lateinit var focusButtons: List<Button>
    private val selectedFocusAreas = mutableSetOf<String>()

    // Variable to store the gender
    private var userGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ensure this matches the XML file name I gave you earlier (female_focus.xml)
        setContentView(R.layout.female_focus)

        // 1. Retrieve Gender from TargetBodyActivity
        userGender = intent.getStringExtra("USER_GENDER")

        // Initialize buttons (Matching IDs from female_focus.xml)
        val btnBicep = findViewById<Button>(R.id.btnBicep)
        val btnAbs = findViewById<Button>(R.id.btnAbs)
        val btnLeg = findViewById<Button>(R.id.btnLeg)
        val btnFullBody = findViewById<Button>(R.id.btnFullBody)

        // Note: Tricep might not be in the XML, so we check if it exists or exclude it
        val btnTricep = findViewById<Button>(R.id.btnTricep)

        // In the female_focus.xml I provided, the ID was btnBackNav
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnNext = findViewById<Button>(R.id.btnNext)

        // Add existing buttons to list (filterNotNull handles if Tricep is missing in XML)
        val allButtons = listOf(btnBicep, btnAbs, btnLeg, btnFullBody, btnTricep)
        focusButtons = allButtons.filterNotNull()

        // Set click listeners
        focusButtons.forEach { button ->
            button.setOnClickListener {
                toggleFocus(button)
            }
        }

        // Back button
        btnBack.setOnClickListener {
            onBackPressed()
        }

        // Next button
        btnNext.setOnClickListener {
            if (selectedFocusAreas.isEmpty()) {
                Toast.makeText(this, "Please select at least one focus area", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, MetricsInputActivity::class.java)

                // Pass Focus Areas
                intent.putStringArrayListExtra("FOCUS_AREAS", ArrayList(selectedFocusAreas))

                // 2. Pass Gender to the next activity
                intent.putExtra("USER_GENDER", userGender)

                startActivity(intent)
            }
        }
    }

    private fun toggleFocus(button: Button) {
        val focus = button.text.toString()
        if (selectedFocusAreas.contains(focus)) {
            // Deselect
            selectedFocusAreas.remove(focus)
            // UPDATED: Dark Grey (Unselected) to match app theme
            button.setBackgroundColor(Color.parseColor("#333333"))
            button.setTextColor(Color.WHITE)
        } else {
            // Select
            selectedFocusAreas.add(focus)
            // UPDATED: Lime Green (Selected) to match app theme
            button.setBackgroundColor(Color.parseColor("#B4F656"))
            button.setTextColor(Color.BLACK)
        }
    }
}