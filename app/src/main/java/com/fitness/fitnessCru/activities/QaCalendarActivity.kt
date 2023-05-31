package com.fitness.fitnessCru.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fitness.fitnessCru.body.UpdateQuestionBody
import com.fitness.fitnessCru.databinding.ActivityQaCalendarBinding
import com.fitness.fitnessCru.utility.DEVICE_TOKEN
import com.google.firebase.messaging.FirebaseMessaging
import com.orhanobut.hawk.Hawk


class QaCalendarActivity : AppCompatActivity() {
    private var calendarBinding: ActivityQaCalendarBinding? = null
    private lateinit var dob: String
    lateinit var deviceToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        calendarBinding = ActivityQaCalendarBinding.inflate(layoutInflater)
        setContentView(calendarBinding!!.root)

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    deviceToken = token.toString()
                    Hawk.put(DEVICE_TOKEN, deviceToken)


                } else {
                    Log.v(
                        "LoginMainActivity",
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    deviceToken = ""
                }
                Log.v("FCMToken", deviceToken)
            }

        calendarBinding!!.dateP.maxDate = System.currentTimeMillis() - (12 * 31_556_952_000)
        setProgressValue(0)
        setBtnProgress()

        calendarBinding!!.skipTv.setOnClickListener {
            startActivity(
                Intent(
                    this@QaCalendarActivity,
                    DashboardActivity::class.java
                )

            )
            Hawk.put(DEVICE_TOKEN, deviceToken)
            OpponentsActivity.start(this@QaCalendarActivity)
            finish()
        }

        goBack()
    }

    private fun setBtnProgress() {
        calendarBinding!!.nextBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                setDOB()
                var bundle = Bundle()
                var data = UpdateQuestionBody("01/01/2001", "", "0.0", "0.0", 0, 0, 0, 0, 0)
                data.dob = dob
                bundle.putSerializable("data", data)
                intent = Intent(this@QaCalendarActivity, QaGenderActivity::class.java)
                intent.putExtras(bundle)
                setProgressValue(13)
                startActivity(intent)
            }
        })
    }

    private fun setProgressValue(i: Int) {
        calendarBinding!!.progressIndicator.progress = i
    }

    private fun setDOB() {
        val day: Int = calendarBinding!!.dateP.dayOfMonth
        val month: Int = calendarBinding!!.dateP.month + 1
        val year: Int = calendarBinding!!.dateP.year
        this@QaCalendarActivity.dob = "$year-$month-$day"
    }

    private fun goBack() {
        calendarBinding!!.gobackbtn.setOnClickListener {
            /* onBackPressed()
             finishAffinity()*/
            startActivity(Intent(this, SplashActivity::class.java))
        }
    }

}