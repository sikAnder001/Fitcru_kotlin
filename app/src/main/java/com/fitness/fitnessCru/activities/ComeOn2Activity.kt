package com.fitness.fitnessCru.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.ActivityComeOn2Binding

class ComeOn2Activity : AppCompatActivity() {
    private var binding: ActivityComeOn2Binding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_come_on2)
        run()
        binding!!.meter.setOnTouchListener { view: View?, motionEvent: MotionEvent? -> run() }
        binding!!.indicator.setOnTouchListener { view: View?, motionEvent: MotionEvent? -> run() }
        continueActivity()
        binding!!.apply {
            backBtn.setOnClickListener {
                startActivity(
                    Intent(
                        this@ComeOn2Activity,
                        ComeOn1Activity::class.java
                    )
                )
            }
            skip.setOnClickListener {
                startActivity(
                    Intent(this@ComeOn2Activity, DashboardActivity::class.java)
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

    private fun continueActivity() {
        startActivity(Intent(this@ComeOn2Activity, DashboardActivity::class.java))
    }
}
