package com.fitness.fitnessCru.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.FitnessLevelAdapter
import com.fitness.fitnessCru.body.UpdateQuestionBody
import com.fitness.fitnessCru.databinding.ActivityQaFitnessLevelBinding
import com.fitness.fitnessCru.interfaces.QuestionaryInterface
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.DEVICE_TOKEN
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.FitnessLevelViewModel
import com.fitness.fitnessCru.vm_factory.FitnessLevelVMFactory
import com.google.firebase.messaging.FirebaseMessaging
import com.orhanobut.hawk.Hawk

class QaFitnessLevelActivity : AppCompatActivity() {

    var binding: ActivityQaFitnessLevelBinding? = null
    private lateinit var loading: CustomProgressLoading
    private var id: Int = 0
    lateinit var deviceToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@QaFitnessLevelActivity,
            R.layout.activity_qa_fitness_level
        )
        setProgressValue(65)
        loading = CustomProgressLoading(this)
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

        binding!!.apply {
            progressIndicator.setOnClickListener {
                continueToNext()
            }
            backBtn.setOnClickListener {
                onBackPressed()
            }
            skip.setOnClickListener {
                startActivity(
                    Intent(this@QaFitnessLevelActivity, DashboardActivity::class.java)
                )
                Hawk.put(DEVICE_TOKEN, deviceToken)
                OpponentsActivity.start(this@QaFitnessLevelActivity)
                finish()


            }
        }
        getFitnessLevel()
    }

    private fun getFitnessLevel() {
        try {
            val adapter by lazy { FitnessLevelAdapter(this@QaFitnessLevelActivity) }
            val repository by lazy { Repository() }
            val factory by lazy { FitnessLevelVMFactory(repository) }
            val viewModel by lazy {
                ViewModelProvider(
                    this,
                    factory
                ).get(FitnessLevelViewModel::class.java)
            }
            binding?.rvFitnessLevel?.adapter = adapter

            adapter.setOnClick(object : QuestionaryInterface {
                override fun onClick(id: Int) {
                    this@QaFitnessLevelActivity.id = id
                }
            })
            loading.showProgress()
            viewModel.getFitnessLevel()
            viewModel.response.observe(this) {
                loading.hideProgress()
                if (it.isSuccessful && it.body()!!.error_code == 0) {
                    adapter.setList(it.body()!!.data)
                } else Util.toast(this, "Something went wrong")
            }
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "getDietType: $e")
        }
    }

    private fun setProgressValue(i: Int) {
        binding!!.progressIndicator.progress = i
    }

    private fun continueToNext() {
        if (id != 0) {
            setProgressValue(78)
            var bundle = Bundle()
            var data = intent.extras!!.getSerializable("data") as UpdateQuestionBody
            data.fitness_level_select_id = id
            bundle.putSerializable("data", data)
            var intent = Intent(this@QaFitnessLevelActivity, QaHowActiveActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)

        } else Toast.makeText(this, "Please select an option", Toast.LENGTH_LONG).show()
    }

}