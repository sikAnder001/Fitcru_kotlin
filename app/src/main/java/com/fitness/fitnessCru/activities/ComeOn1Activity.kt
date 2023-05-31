package com.fitness.fitnessCru.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.ActivityComeOn1Binding

class ComeOn1Activity : AppCompatActivity() {
    private var binding: ActivityComeOn1Binding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_come_on1)
        binding!!.apply {
            tvContinue.setOnClickListener {
                startActivity(Intent(applicationContext, ComeOn2Activity::class.java))
            }
            run()
            meter.setOnTouchListener { view: View?, motionEvent: MotionEvent? -> run() }
            indicator.setOnTouchListener { view: View?, motionEvent: MotionEvent? -> run() }
        }
        binding!!.apply {
            backBtn.setOnClickListener {
                startActivity(
                    Intent(
                        this@ComeOn1Activity,
                        CongratsActivity::class.java
                    )
                )
            }
            skip.setOnClickListener {
                startActivity(
                    Intent(this@ComeOn1Activity, DashboardActivity::class.java)
                )
                finish()
            }
        }
    }

    fun run(): Boolean {
        val i = intArrayOf(-90)
        object : CountDownTimer(10000, 10) {
            override fun onTick(l: Long) {
                if (i[0] >= 90) return else binding!!.indicator.rotation = i[0].toFloat()
                i[0] = i[0] + 1
            }

            override fun onFinish() {}
        }.start()
        return true
    }
}
