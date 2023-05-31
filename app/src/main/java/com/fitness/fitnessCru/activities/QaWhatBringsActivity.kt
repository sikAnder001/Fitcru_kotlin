package com.fitness.fitnessCru.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.WhatBringsYouAdapter
import com.fitness.fitnessCru.body.UpdateQuestionBody
import com.fitness.fitnessCru.databinding.ActivityQaWhatBringsBinding
import com.fitness.fitnessCru.interfaces.QuestionaryInterface
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.DEVICE_TOKEN
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.WhatBringsYouViewModel
import com.fitness.fitnessCru.vm_factory.WhatBringsYouVMFactory
import com.google.firebase.messaging.FirebaseMessaging
import com.orhanobut.hawk.Hawk

class QaWhatBringsActivity : AppCompatActivity() {

    private var binding: ActivityQaWhatBringsBinding? = null
    private var TAG = QaWhatBringsActivity::class.java.simpleName
    private lateinit var whatBringsYouAdapter: WhatBringsYouAdapter
    private lateinit var loading: CustomProgressLoading
    private var id: Int = 0
    lateinit var deviceToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qa_what_brings)
        binding = ActivityQaWhatBringsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        loading = CustomProgressLoading(this)
        setProgressValue(39)
        setWhatBringsYou()
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

        binding!!.skipTv.setOnClickListener {
            startActivity(
                Intent(
                    this@QaWhatBringsActivity,
                    DashboardActivity::class.java
                )
            )
            Hawk.put(DEVICE_TOKEN, deviceToken)
            OpponentsActivity.start(this@QaWhatBringsActivity)
            finish()
        }

        goBack()

        binding!!.progressIndicator.setOnClickListener {
            continueToNext()
        }
        getWhatBringsYouData()
    }

    private fun getWhatBringsYouData() {
        val repository by lazy { Repository() }
        val factory by lazy { WhatBringsYouVMFactory(repository) }
        val viewModel by lazy {
            ViewModelProvider(
                this,
                factory
            ).get(WhatBringsYouViewModel::class.java)
        }
        loading.showProgress()
        viewModel.getWhatBringsYou()
        viewModel.response.observe(this) {
            loading.hideProgress()
            try {
                val token = Session.getToken()
                Log.e(TAG, "createData: $token")
                if (it.isSuccessful && it.body()?.error_code == 0) {
                    whatBringsYouAdapter.setListData(it.body()!!.data)
                    Log.e(TAG, "${it.body()},${it.message()}")
                } else Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Util.toast(this, "Error : ${e.message}")
            }
        }
    }

    private fun setWhatBringsYou() {
        val gridLayout = GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
        binding!!.bringsYouToFitcru.layoutManager = gridLayout
        binding!!.bringsYouToFitcru.setHasFixedSize(true)
        whatBringsYouAdapter = WhatBringsYouAdapter(this)
        whatBringsYouAdapter.setOnClick(object : QuestionaryInterface {
            override fun onClick(id: Int) {
                this@QaWhatBringsActivity.id = id
            }
        })
        binding!!.bringsYouToFitcru.adapter = whatBringsYouAdapter
    }

    private fun setProgressValue(i: Int) {
        binding!!.progressIndicator.progress = i
    }

    private fun continueToNext() {
        if (id != 0) {
            var bundle = Bundle()
            var data = intent.extras!!.getSerializable("data") as UpdateQuestionBody
            data.what_bring_select_id = id
            bundle.putSerializable("data", data)
            var intent = Intent(this@QaWhatBringsActivity, QaDietTypeActivity::class.java)
            setProgressValue(52)
            intent.putExtras(bundle)
            startActivity(intent)
        } else Toast.makeText(this, "Please select an option", Toast.LENGTH_LONG).show()
    }

    private fun goBack() {
        binding!!.gobackbtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                onBackPressed()
            }
        })
    }

}
