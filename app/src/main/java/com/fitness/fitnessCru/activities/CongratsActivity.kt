package com.fitness.fitnessCru.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.ActivityCongratsBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.CongratulationResponse
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.DEVICE_TOKEN
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.CongratulationsViewModel
import com.fitness.fitnessCru.vm_factory.CongratulationsVMFactory
import com.google.firebase.messaging.FirebaseMessaging
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_congrats.*

//congrats
class CongratsActivity : AppCompatActivity() {
    private var binding: ActivityCongratsBinding? = null
    private lateinit var loading: CustomProgressLoading
    private var flag = false
    lateinit var deviceToken: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_congrats)
        loading = CustomProgressLoading(this@CongratsActivity)


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

        binding!!.tvContinue.setOnClickListener { v: View? ->
            if (flag) {
                val intent = Intent(this, SubscriptionPlanActivity::class.java)
                intent.putExtra("num", 0)
                intent.putExtra("num2", 1)
                startActivity(intent)
                finish()
            }
        }

        loading.showProgress()
        getCongratsData()
        binding!!.apply {
            backBtn.setOnClickListener {
                onBackPressed()
            }
            skip.setOnClickListener {
                startActivity(
                    Intent(this@CongratsActivity, DashboardActivity::class.java)
                )
                Hawk.put(DEVICE_TOKEN, deviceToken)
                OpponentsActivity.start(this@CongratsActivity)
                finish()
                finish()
            }
        }
    }

    private fun getCongratsData() {
        val repository by lazy { Repository() }
        val congratulationsVMFactory by lazy { CongratulationsVMFactory(repository) }
        val congratulationsViewModel by lazy {
            ViewModelProvider(
                this,
                congratulationsVMFactory
            ).get(CongratulationsViewModel::class.java)
        }
        congratulationsViewModel.getCongrats()
        congratulationsViewModel.response.observe(this) {
            loading.hideProgress()
            if (it.isSuccessful && it.body()!!.error_code == 0) {
                flag = true
                //   Util.toast(this@CongratsActivity, it.body()!!.message)
                binding.apply {
                    tvBMI.text = "BMI ${it.body()?.data?.bmi_value?.bmi}"
                    tvTDee.text = "${it.body()?.data?.bmiCalculation?.tdee}"
                    tvBMR.text = "${it.body()?.data?.bmiCalculation?.bmr}"

                    val bmi: Double = it.body()?.data?.bmi_value?.bmi!!.toDouble()


                    if (bmi <= 16.0) {
                        indicator.setRotation(-90f)
                    } else if (bmi <= 18.5) {
                        if (bmi == 18.5) indicator.setRotation(-48f) else if (bmi <= 16.1 || bmi <= 16.5f)
                            indicator.setRotation(-85f)
                        else if (bmi <= 16.5 || bmi <= 17f) indicator.setRotation(-70f) else if (bmi <= 17.1 || bmi <= 17.5) indicator.setRotation(
                            -65f
                        ) else indicator.setRotation(-48f)
                    } else if (bmi <= 25.0) {
                        if (bmi == 25.0) indicator.setRotation(0f) else if (bmi <= 18.6 || bmi <= 19.5) indicator.setRotation(
                            -40f
                        ) else if (bmi <= 19.6 || bmi <= 20.5) indicator.setRotation(-25f) else if (bmi <= 21.6 || bmi <= 22.5) indicator.setRotation(
                            -20f
                        ) else if (bmi <= 22.6 || bmi <= 23.5) indicator.setRotation(
                            -10f
                        ) else if (bmi <= 23.6 || bmi <= 24.99) indicator.setRotation(
                            -5f
                        ) else indicator.setRotation(0f)
                    } else if (bmi <= 40.0) {
                        if (bmi == 40.0) indicator.setRotation(48f) else if (bmi <= 25.1 || bmi <= 25.5) indicator.setRotation(
                            5f
                        )
                        else if (bmi <= 25.6 || bmi <= 26f) indicator.setRotation(10f) else if (bmi <= 26 || bmi <= 28) indicator.setRotation(
                            15f
                        ) else if (bmi <= 28.1 || bmi <= 30) indicator.setRotation(
                            20f
                        )
                        else if (bmi <= 30.1 || bmi <= 35.5) indicator.setRotation(25f) else if (bmi <= 36 || bmi <= 37.5) indicator.setRotation(
                            30f

                        ) else if (bmi <= 37.9 || bmi <= 38.5) indicator.setRotation(
                            35f
                        ) else if (bmi <= 38.6 || bmi <= 39.99) indicator.setRotation(
                            40f
                        ) else indicator.setRotation(48f)

                    } else if (bmi <= 50.0)
                        if (bmi == 50.0) indicator.setRotation(90f) else if (bmi <= 40.1 || bmi <= 45.5) indicator.setRotation(
                            55f
                        )
                        else if (bmi <= 45.6 || bmi <= 47f) indicator.setRotation(70f) else if (bmi <= 47.1 || bmi <= 48.5) indicator.setRotation(
                            75f
                        ) else if (bmi <= 48.6 || bmi <= 48.9) indicator.setRotation(
                            80f
                        ) else if (bmi <= 49 || bmi <= 49.99) indicator.setRotation(
                            85f
                        ) else indicator.setRotation(90f)
                    else {
                        indicator.setRotation(90f)
                    }


                    /*  if (bmi <= 16.0) {
                          indicator.setRotation(-90f)
                      } else if (bmi <= 18.5) {
                          if (bmi == 18.5) indicator.setRotation(-60f) else if (bmi <= 16.1 || bmi <= 16.5f)
                              indicator.setRotation(-80f)
                          else if (bmi <= 16.5 || bmi <= 17f) indicator.setRotation(-73f) else if (bmi <= 17.1 || bmi <= 17.5) indicator.setRotation(
                              -66f
                          ) else indicator.setRotation(-62f)
                      } else if (bmi <= 25.0) {
                          if (bmi == 25.0) indicator.setRotation(40f) else if (bmi <= 18.6 || bmi <= 19.5) indicator.setRotation(
                              -20f
                          ) else if (bmi <= 19.6 || bmi <= 20.5) indicator.setRotation(-10f) else if (bmi <= 21.6 || bmi <= 22.5) indicator.setRotation(
                              0f
                          ) else if (bmi <= 22.6 || bmi <= 23.5) indicator.setRotation(
                              10f
                          ) else if (bmi <= 23.6 || bmi <= 24.5) indicator.setRotation(
                              20f
                          ) else indicator.setRotation(25f)
                      } else if (bmi <= 40.0) {
                          if (bmi <= 25.5) indicator.setRotation(30f) else if (bmi <= 25.6 || bmi <= 30.5) indicator.setRotation(
                              50f
                          ) else if (bmi <= 30.6 || bmi <= 35.5) indicator.setRotation(
                              70f
                          ) else if (bmi <= 35.6 || bmi <= 39.0) indicator.setRotation(
                              80f
                          ) else indicator.setRotation(90f)
                      } else {
                          indicator.setRotation(90f)
                      }*/


                }
            } else if (!it.isSuccessful) {
                try {
                    var response = Util.error(it.errorBody(), CongratulationResponse::class.java)
                    Util.toast(this, response.message)
                } catch (e: Exception) {
                    Util.toast(this, "Error: Something went wrong")
                }
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
