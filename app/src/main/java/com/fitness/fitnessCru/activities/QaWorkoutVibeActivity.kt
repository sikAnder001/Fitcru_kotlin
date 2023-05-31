package com.fitness.fitnessCru.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.QaWorkoutVibeAdapter
import com.fitness.fitnessCru.body.UpdateQuestionBody
import com.fitness.fitnessCru.databinding.ActivityQaWorkoutVibeBinding
import com.fitness.fitnessCru.interfaces.QuestionaryInterface
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.SignUpWithEmailResponse
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.DEVICE_TOKEN
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.WorkoutVibeViewModel
import com.fitness.fitnessCru.vm_factory.WorkoutVibeVMFactory
import com.google.firebase.messaging.FirebaseMessaging
import com.orhanobut.hawk.Hawk

class QaWorkoutVibeActivity : AppCompatActivity() {

    private lateinit var workoutVibeBinding: ActivityQaWorkoutVibeBinding
    private var TAG = QaWorkoutVibeActivity::class.java.simpleName
    private lateinit var workoutVibeAdapter: QaWorkoutVibeAdapter
    private lateinit var loading: CustomProgressLoading
    private var id: Int = 0
    lateinit var deviceToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workoutVibeBinding = DataBindingUtil.setContentView(this, R.layout.activity_qa_workout_vibe)
        loading = CustomProgressLoading(this)
        setProgressValue(91)
        setWorkoutVibeRV()
        getWorkoutVibeData()
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


        workoutVibeBinding!!.apply {
            gobackbtn.setOnClickListener {
                onBackPressed()
            }
            skipTv.setOnClickListener {
                startActivity(
                    Intent(this@QaWorkoutVibeActivity, DashboardActivity::class.java)
                )
                Hawk.put(DEVICE_TOKEN, deviceToken)
                OpponentsActivity.start(this@QaWorkoutVibeActivity)
                finish()

            }
        }
    }

    private fun getWorkoutVibeData() {
        val repository by lazy { Repository() }
        val factory by lazy { WorkoutVibeVMFactory(repository) }
        val viewModel by lazy {
            ViewModelProvider(
                this,
                factory
            ).get(WorkoutVibeViewModel::class.java)
        }
        workoutVibeBinding!!.progressIndicator.setOnClickListener {
            continueToNext(viewModel)
        }
        loading.showProgress()
        viewModel.getWorkoutVibe()
        viewModel.response.observe(this) {
            loading.hideProgress()
            try {
                val token = Session.getToken()
                Log.e(TAG, "$token")
                if (it.isSuccessful && it.body()?.error_code == 0) {
                    workoutVibeAdapter.setWorkoutVibeData(it.body()!!.data)
                    Log.e(TAG, "${it.body()},${it.message()}")
                } else Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Util.toast(this, "Error : ${e.message}")
            }
        }
    }

    private fun setWorkoutVibeRV() {
        val gridLayout = GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
        workoutVibeBinding.workoutVibeRv.layoutManager = gridLayout
        workoutVibeBinding.workoutVibeRv.setHasFixedSize(true)
        workoutVibeAdapter = QaWorkoutVibeAdapter(this)
        workoutVibeAdapter.setOnClick(object : QuestionaryInterface {
            override fun onClick(id: Int) {
                this@QaWorkoutVibeActivity.id = id
            }
        })
        workoutVibeBinding.workoutVibeRv.adapter = workoutVibeAdapter

    }

    private fun setProgressValue(i: Int) {
        workoutVibeBinding!!.progressIndicator.progress = i
    }

    private fun continueToNext(viewModel: WorkoutVibeViewModel) {
        if (id != 0) {
            setProgressValue(100)
            var bundle = Bundle()
            var data = intent.extras!!.getSerializable("data") as UpdateQuestionBody
            data.workout_vibe_select_id = id
            bundle.putSerializable("data", data)

            viewModel.setQuesionareSelect(data)
            viewModel.response1.observe(this@QaWorkoutVibeActivity) {
                loading.hideProgress()
                val res = it.body()
                try {
                    if (res != null && it.isSuccessful && res.error_code ?: 1 == 0) {
                        Toast.makeText(
                            this,
                            res.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        var intent =
                            Intent(this@QaWorkoutVibeActivity, CongratsActivity::class.java)
                        startActivity(intent)
                    } else {
                        if (it.code() == 400 || it.code() == 401) {
                            val error =
                                Util.error(it.errorBody(), SignUpWithEmailResponse::class.java)
                            Toast.makeText(
                                this,
                                error.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@QaWorkoutVibeActivity,
                        "Error : ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else Toast.makeText(this, "Please select an option", Toast.LENGTH_LONG).show()
    }
}
