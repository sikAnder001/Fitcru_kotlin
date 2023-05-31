package com.fitness.fitnessCru.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.DietTypeAdapter
import com.fitness.fitnessCru.body.UpdateQuestionBody
import com.fitness.fitnessCru.databinding.ActivityQaDietTypeBinding
import com.fitness.fitnessCru.interfaces.QuestionaryInterface
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.DEVICE_TOKEN
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.DietTypeViewModel
import com.fitness.fitnessCru.vm_factory.DietTypeVMFactory
import com.google.firebase.messaging.FirebaseMessaging
import com.orhanobut.hawk.Hawk

class QaDietTypeActivity : AppCompatActivity() {
    private var binding: ActivityQaDietTypeBinding? = null
    private lateinit var loading: CustomProgressLoading
    private var id: Int = 0
    lateinit var deviceToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qa_diet_type)
        loading = CustomProgressLoading(this)

        setProgressValue(52)
        getDietType()
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
            gobackbtn.setOnClickListener {
                onBackPressed()
            }
            skipTv.setOnClickListener {
                startActivity(
                    Intent(this@QaDietTypeActivity, DashboardActivity::class.java)
                )
                Hawk.put(DEVICE_TOKEN, deviceToken)
                OpponentsActivity.start(this@QaDietTypeActivity)
                finish()
            }
        }

    }

    private fun continueToNext() {
        if (id != 0) {
            setProgressValue(65)
            var bundle = Bundle()
            var x = intent.extras!!.getSerializable("data") as UpdateQuestionBody
            x.diet_type_select_id = id
            bundle.putSerializable("data", x)
            var intent = Intent(this@QaDietTypeActivity, QaFitnessLevelActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)

        } else Toast.makeText(this, "Please select an option", Toast.LENGTH_LONG).show()
    }

    private fun getDietType() {
        try {
            val adapter by lazy { DietTypeAdapter(this@QaDietTypeActivity) }
            val repository by lazy { Repository() }
            val factory by lazy { DietTypeVMFactory(repository) }
            val viewModel by lazy {
                ViewModelProvider(
                    this,
                    factory
                ).get(DietTypeViewModel::class.java)
            }
            binding?.rvDietType?.adapter = adapter

            adapter.setOnClick(object : QuestionaryInterface {
                override fun onClick(id: Int) {
                    this@QaDietTypeActivity.id = id
                }
            })
            loading.showProgress()
            viewModel.getDietType()
            viewModel.response.observe(this) {
                loading.hideProgress()
                if (it.isSuccessful && it.body()!!.error_code == 0) {
                    adapter.setList(it.body()!!.data)
                } else Util.toast(this, "Something went wrong")
            }
        } catch (e: Exception) {
            Log.e(TAG, "getDietType: $e")
        }
    }

    private fun setProgressValue(i: Int) {
        binding!!.progressIndicator.progress = i
    }
}