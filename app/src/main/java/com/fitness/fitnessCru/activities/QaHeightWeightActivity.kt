package com.fitness.fitnessCru.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fitness.fitnessCru.body.UpdateQuestionBody
import com.fitness.fitnessCru.databinding.ActivityQaHeightWeightBinding
import com.fitness.fitnessCru.utility.DEVICE_TOKEN
import com.google.firebase.messaging.FirebaseMessaging
import com.orhanobut.hawk.Hawk

class QaHeightWeightActivity : AppCompatActivity() {
    private var binding: ActivityQaHeightWeightBinding? = null

    private var height: String = ""
    private var weight: String = ""
    lateinit var deviceToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_questionary_three);
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



        binding =
            ActivityQaHeightWeightBinding.inflate(
                layoutInflater
            )
        binding!!.skipTv.setOnClickListener {
            startActivity(
                Intent(
                    this@QaHeightWeightActivity,
                    DashboardActivity::class.java
                )
            )
            Hawk.put(DEVICE_TOKEN, deviceToken)
            OpponentsActivity.start(this@QaHeightWeightActivity)
            finish()
        }

        setContentView(binding!!.root)
        goBack()
        setProgressValue(26)
        setHeightWeight()
        binding!!.nextBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if (this@QaHeightWeightActivity.height.isNotEmpty() && this@QaHeightWeightActivity.weight.isNotEmpty()) {
                    setProgressValue(39)
                    var bundle = Bundle()
                    var data = intent.extras!!.getSerializable("data") as UpdateQuestionBody
                    data.height = this@QaHeightWeightActivity.height
                    data.weight = this@QaHeightWeightActivity.weight
                    bundle.putSerializable("data", data)
                    var intent =
                        Intent(this@QaHeightWeightActivity, QaWhatBringsActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@QaHeightWeightActivity,
                        "Please select height & weight",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun setProgressValue(i: Int) {
        binding!!.progressIndicator.progress = i
    }

    private fun setHeightWeight() {
        binding!!.ruleViewHeight.setOnValueChangedListener { value ->
            val height = value.toString()
            this@QaHeightWeightActivity.height = height
            binding!!.pickFT.text = height.replace(".", "'") + '"'
        }

        binding!!.ruleViewweight.setOnValueChangedListener { value ->
            binding!!.pickKG.text = value.toString()
            this@QaHeightWeightActivity.weight = binding!!.pickKG.text as String
        }
    }

    private fun goBack() {
        binding!!.gobackbtn.setOnClickListener {
            onBackPressed()
        }
    }

}