package com.fitness.fitnessCru.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.ProfilePageAdapter
import com.fitness.fitnessCru.databinding.ActivityEditProfileBinding
import com.fitness.fitnessCru.utility.Session
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    var snc: String? = null
    var snc2: String? = null
    var pview: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)

        snc = intent.getStringExtra("pos")
        pview = intent.getStringExtra("view_p")

        snc2 = intent.getStringExtra("pos2")

        val c: Calendar = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        val curDate = df.format(c.getTime())

        val userDetail = Session.getUserDetails()
        var answer = ""
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val realCurdate: Date = simpleDateFormat.parse(curDate)

        if (userDetail.planDetail != null) {

            for (element in userDetail.planDetail) {
                if (element.subscription_plan_id == 3 || element.subscription_plan_id == 4) {
                    val expiryDate: Date = simpleDateFormat.parse(element.end_date)
                    if (expiryDate > realCurdate) {
                        answer = "yes"
                    }
                }
            }
        }

        binding.apply {
            viewPager.adapter = ProfilePageAdapter(this@EditProfileActivity, answer)
            TabLayoutMediator(tabLayout, viewPager, true, false) { tab, position ->
                tab.text = if (answer == "yes") {
                    arrayOf(
                        " Basic ",
                        " Health ",
                        " Health Questionnaire ",
                        " Address ",
                        " Password "
                    )
                } else {
                    arrayOf(
                        " Basic ",
                        " Health ",
                        " Address ",
                        " Password "
                    )
                }[position]
                //viewPager.setCurrentItem(position, false)TODO for smoothScrolling
            }.attach()
            if (snc != null) {
                tabLayout.getTabAt(1)!!.select()
                /* binding.gobackbtn.setOnClickListener {
                     finish()

                 }*/

            } else if (snc2 != null) {
                tabLayout.getTabAt(2)!!.select()
                /*  binding.gobackbtn.setOnClickListener {
                      finish()

                  }*/

            }
        }

        binding.gobackbtn.setOnClickListener {
            if (pview != null) {
                startActivity(
                    Intent(
                        applicationContext,
                        ViewProfileActivity::class.java
                    )
                )
                finish()

            } else {

                finish()

            }

        }

    }
}