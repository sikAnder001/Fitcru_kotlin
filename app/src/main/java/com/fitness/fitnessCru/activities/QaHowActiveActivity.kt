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
import com.fitness.fitnessCru.adapter.HowActiveYouAdapter
import com.fitness.fitnessCru.body.UpdateQuestionBody
import com.fitness.fitnessCru.databinding.ActivityQaHowActiveBinding
import com.fitness.fitnessCru.interfaces.QuestionaryInterface
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.DEVICE_TOKEN
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.HowActiveYouViewModel
import com.fitness.fitnessCru.vm_factory.HowActiveYouVMFactory
import com.google.firebase.messaging.FirebaseMessaging
import com.orhanobut.hawk.Hawk

class QaHowActiveActivity : AppCompatActivity() {
    private var binding: ActivityQaHowActiveBinding? = null
    private var TAG = QaHowActiveActivity::class.java.simpleName
    private lateinit var howActiveYouAdapter: HowActiveYouAdapter
    private lateinit var loading: CustomProgressLoading
    private var id: Int = 0
    lateinit var deviceToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQaHowActiveBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        loading = CustomProgressLoading(this)
        getHowActiveData()
        setProgressValue(78)
        setHowActiveYou()
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


        goBack()
        binding!!.skip.setOnClickListener {
            startActivity(
                Intent(this@QaHowActiveActivity, DashboardActivity::class.java)
            )
            Hawk.put(DEVICE_TOKEN, deviceToken)
            OpponentsActivity.start(this@QaHowActiveActivity)
            finish()

        }
        binding!!.progressIndicator.setOnClickListener {
            continueToNext()
        }
    }

    private fun getHowActiveData() {
        val repository by lazy { Repository() }
        val factory by lazy { HowActiveYouVMFactory(repository) }
        val viewModel by lazy {
            ViewModelProvider(
                this,
                factory
            ).get(HowActiveYouViewModel::class.java)
        }
        loading.showProgress()
        viewModel.getHowActiveYou()
        viewModel.response.observe(this) {
            loading.hideProgress()
            try {
                val token = Session.getToken()
                Log.e(TAG, "createDataHow: $token")
                if (it.isSuccessful && it.body()?.error_code == 0) {
                    howActiveYouAdapter.setListData(it.body()!!.data)
                    Log.e(TAG, "${it.body()},${it.message()}")
                } else Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Util.toast(this, "Error : ${e.message}")
            }
        }
    }

    private fun setHowActiveYou() {
        val gridLayout = GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false)
        binding!!.howActiveYouRv.layoutManager = gridLayout
        binding!!.howActiveYouRv.setHasFixedSize(true)
        howActiveYouAdapter = HowActiveYouAdapter(this)
        binding!!.howActiveYouRv.adapter = howActiveYouAdapter

        howActiveYouAdapter.setOnClick(object : QuestionaryInterface {
            override fun onClick(id: Int) {
                this@QaHowActiveActivity.id = id
            }
        })
    }

    private fun goBack() {
        binding!!.backBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                onBackPressed()
            }
        })
    }

    private fun setProgressValue(i: Int) {
        binding!!.progressIndicator.progress = i
    }

    private fun continueToNext() {
        if (id != 0) {
            setProgressValue(91)
            var bundle = Bundle()
            var data = intent.extras!!.getSerializable("data") as UpdateQuestionBody
            data.how_active_select_id = id
            bundle.putSerializable("data", data)
            var intent = Intent(this@QaHowActiveActivity, QaWorkoutVibeActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)

        } else Toast.makeText(this, "Please select an option", Toast.LENGTH_LONG).show()
    }
}