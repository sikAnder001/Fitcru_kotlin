package com.fitness.fitnessCru.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.ActivityTermsAndConditionsBinding

class TermsAndConditionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermsAndConditionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_terms_and_conditions)
        binding.apply {
            backBtn.setOnClickListener {
                onBackPressed()
                finish()
            }

            pdfView1.fromAsset("fitcru_terms_of_use.pdf").load()
        }
    }
}