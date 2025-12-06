package com.example.fitx

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fitx.databinding.MetricsInputBinding
import kotlin.math.roundToInt

class MetricsInputActivity : AppCompatActivity() {

    private lateinit var binding: MetricsInputBinding

    // Variable to hold gender
    private var userGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MetricsInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Retrieve Gender from previous screen (FocusAreaActivity)
        userGender = intent.getStringExtra("USER_GENDER")

        setupSliders()

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // NEXT Button
        binding.btnNext.setOnClickListener {
            val finalWeight = binding.weightSlider.value.roundToInt()
            val finalHeight = binding.heightSlider.value.roundToInt()

            val intent = Intent(this, MotivationsActivity::class.java)
            intent.putExtra("USER_WEIGHT", finalWeight)
            intent.putExtra("USER_HEIGHT", finalHeight)

            // 2. Pass Gender to the next screen (MotivationsActivity)
            intent.putExtra("USER_GENDER", userGender)

            startActivity(intent)
        }
    }

    private fun setupSliders() {
        binding.weightSlider.addOnChangeListener { _, value, _ ->
            val weight = value.roundToInt()
            binding.weightValue.text = "$weight kgs"
        }

        binding.heightSlider.addOnChangeListener { _, value, _ ->
            val totalInches = value.roundToInt()
            val feet = totalInches / 12
            val inches = totalInches % 12
            binding.heightValue.text = "$feet ft $inches in"
        }
        updateInitialValues()
    }

    private fun updateInitialValues() {
        val initialWeight = binding.weightSlider.value.roundToInt()
        binding.weightValue.text = "$initialWeight kgs"
        val totalInches = binding.heightSlider.value.roundToInt()
        val feet = totalInches / 12
        val inches = totalInches % 12
        binding.heightValue.text = "$feet ft $inches in"
    }
}