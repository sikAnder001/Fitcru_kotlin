package com.fitness.fitnessCru.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivacyPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy)
        binding.apply {
            backBtn.setOnClickListener {
                onBackPressed()
                finish()
            }

            pdfView.fromAsset("fitcru_privacy_policy.pdf").load()
        }
    }
}