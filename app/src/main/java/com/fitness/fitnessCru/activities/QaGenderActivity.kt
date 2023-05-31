package com.fitness.fitnessCru.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.body.UpdateQuestionBody
import com.fitness.fitnessCru.databinding.ActivityQaGenderBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.DEVICE_TOKEN
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.UpdateGenderDobViewModel
import com.fitness.fitnessCru.vm_factory.UpdateGenderDobVMFactory
import com.google.firebase.messaging.FirebaseMessaging
import com.orhanobut.hawk.Hawk

//select
class QaGenderActivity : AppCompatActivity() {
    private var gendarBinding: ActivityQaGenderBinding? = null
    private var gender: String = ""
    private lateinit var loading: CustomProgressLoading
    private lateinit var viewModel: UpdateGenderDobViewModel
    private lateinit var data: UpdateQuestionBody
    lateinit var deviceToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gendarBinding = ActivityQaGenderBinding.inflate(layoutInflater)
        setContentView(gendarBinding!!.root)
        gendarBinding!!.maleText.setTextColor(Color.GRAY)
        gendarBinding!!.femaleText.setTextColor(Color.GRAY)
        loading = CustomProgressLoading(applicationContext)
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

        val repository by lazy { Repository() }
        val factory by lazy { UpdateGenderDobVMFactory(repository) }
        viewModel = ViewModelProvider(
            this,
            factory
        ).get(UpdateGenderDobViewModel::class.java)

        gendarBinding!!.skipTv.setOnClickListener {
            startActivity(
                Intent(
                    this@QaGenderActivity,
                    DashboardActivity::class.java
                )
            )
            Hawk.put(DEVICE_TOKEN, deviceToken)
            OpponentsActivity.start(this@QaGenderActivity)
            finish()
        }

        maleG()
        femaleG()
        setProgressValue(13)
        setBtnProgress()

        goBack()
    }

    private fun setBtnProgress() {
        gendarBinding!!.progressIndicator.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if (this@QaGenderActivity.gender.isEmpty()) {
                    Toast.makeText(
                        this@QaGenderActivity,
                        "Please select gender",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    data = intent.extras!!.getSerializable("data") as UpdateQuestionBody
                    data.gender = gender
                    updateGenderDob(if (data.gender.equals("Male", true)) "0" else "1", data.dob)
                }
            }
        })
    }

    private fun updateGenderDob(gender: String, dob: String) {
        loading.showProgress()
        viewModel.updateGenderDob(dob, gender)
        viewModel.response.observe(this) {
            loading.hideProgress()
            try {
                if (it.isSuccessful && it.body()?.error_code == 0) {
                    Session.setToken(it.body()!!.data.access_token)
                    setProgressValue(26)
                    var bundle = Bundle()
                    bundle.putSerializable("data", data)
                    var intent = Intent(this@QaGenderActivity, QaHeightWeightActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                } else if (it.isSuccessful && it.body()?.error_code == 1) Toast.makeText(
                    this,
                    it.body()?.message,
                    Toast.LENGTH_LONG
                ).show()
                else Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Util.toast(this, "Error : ${e.message}")
            }
        }
    }

    private fun setProgressValue(i: Int) {
        gendarBinding!!.progressIndicator.progress = i
    }

    private fun maleG() {
        gendarBinding!!.mailConst.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@QaGenderActivity.gender = "Male"
                gendarBinding!!.maleChecked.visibility = View.VISIBLE
                gendarBinding!!.maleText.setTextColor(Color.BLACK)
                gendarBinding!!.mailConst.setBackgroundResource(R.drawable.after_checked_gender)
                if (gendarBinding!!.maleChecked.visibility == View.VISIBLE) {
                    gendarBinding!!.femaleText.setTextColor(Color.GRAY)
                    gendarBinding!!.femaleChecked.visibility = View.GONE
                    gendarBinding!!.femaleConst.setBackgroundResource(R.drawable.gender_before_check_background)
                }
            }
        })
    }

    private fun femaleG() {
        gendarBinding!!.femaleConst.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@QaGenderActivity.gender = "Female"
                gendarBinding!!.femaleChecked.visibility = View.VISIBLE
                gendarBinding!!.femaleText.setTextColor(Color.BLACK)
                gendarBinding!!.femaleConst.setBackgroundResource(R.drawable.after_checked_gender)
                if (gendarBinding!!.femaleChecked.visibility == View.VISIBLE) {
                    gendarBinding!!.maleText.setTextColor(Color.GRAY)
                    gendarBinding!!.maleChecked.visibility = View.GONE
                    gendarBinding!!.mailConst.setBackgroundResource(R.drawable.gender_before_check_background)
                }
            }
        })
    }


    private fun goBack() {
        gendarBinding!!.gobackbtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                onBackPressed()
            }
        })
    }
}