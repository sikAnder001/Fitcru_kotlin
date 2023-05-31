package com.fitness.fitnessCru.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.SubPlanParentAdapter
import com.fitness.fitnessCru.databinding.ActivitySubscriptionPlanBinding
import com.fitness.fitnessCru.model.SubPlanChildModel
import com.fitness.fitnessCru.model.SubPlanParentModel

class SubscriptionPlanActivity : AppCompatActivity() {
    var pareantModel: ArrayList<SubPlanParentModel>? = null
    var month: ArrayList<SubPlanChildModel>? = null
    var free: ArrayList<SubPlanChildModel>? = null
    var sixMonth: ArrayList<SubPlanChildModel>? = null
    var twelveMonth: ArrayList<SubPlanChildModel>? = null

    private lateinit var subPlanBindnig: ActivitySubscriptionPlanBinding

    private var num = 0
    private var num2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subPlanBindnig = DataBindingUtil.setContentView(this, R.layout.activity_subscription_plan)

        subPlanBindnig.skipTv.setOnClickListener {
            val gotoDashBoard = Intent(this, DashboardActivity::class.java)
            startActivity(gotoDashBoard)
        }

        subPlanBindnig.gobackbtn.setOnClickListener {
            onBackPressed()
        }

        num = intent.getIntExtra("num", 0)
        num2 = intent.getIntExtra("num2", 0)


        pareantModel = ArrayList()
        free = ArrayList()
        month = ArrayList()
        sixMonth = ArrayList()
        twelveMonth = ArrayList()

        month!!.add(SubPlanChildModel(R.drawable.circular_icon, "Everything in FitCru Basic"))
        month!!.add(SubPlanChildModel(R.drawable.circular_icon, "Access To 500+ Workout Classes"))
        month!!.add(
            SubPlanChildModel(
                R.drawable.circular_icon,
                "Access to all Premium Programs and Workouts"
            )
        )
        month!!.add(
            SubPlanChildModel(
                R.drawable.circular_icon,
                "Access to Daily live workout sessions"
            )
        )
        month!!.add(
            SubPlanChildModel(
                R.drawable.circular_icon,
                "Link Fitness Wearables and Track all details"
            )
        )
        pareantModel!!.add(
            SubPlanParentModel(
                2,
                "FitCru Live",
                "₹999",
                " /month",
                "Select",
                month!!,
                R.drawable.sub_plan_background2
            )
        )

        sixMonth!!.add(SubPlanChildModel(R.drawable.circular_icon, "Everything in FitCru Live"))
        sixMonth!!.add(SubPlanChildModel(R.drawable.circular_icon, "2 way access with your coach"))
        sixMonth!!.add(
            SubPlanChildModel(
                R.drawable.circular_icon,
                "Weekly reports to track your progress"
            )
        )
        sixMonth!!.add(
            SubPlanChildModel(
                R.drawable.circular_icon,
                "Customized programs for you designed by your coach"
            )
        )
        pareantModel!!.add(
            SubPlanParentModel(
                3,
                "FitCru Coaching",
                "Starting From 4999",
                " /month",
                "Select",
                sixMonth!!,
                R.drawable.sub_plan_background3
            )
        )

        twelveMonth!!.add(
            SubPlanChildModel(
                R.drawable.circular_icon,
                "Everything in FitCru Coaching"
            )
        )
        twelveMonth!!.add(
            SubPlanChildModel(
                R.drawable.circular_icon,
                "Fully customised bespoke solution"
            )
        )
        twelveMonth!!.add(
            SubPlanChildModel(
                R.drawable.circular_icon,
                "Designed specifically for Elite athletes and Celebrities"
            )
        )
        pareantModel!!.add(
            SubPlanParentModel(
                4,
                "FitCru Select",
                "Starting from 75000",
                " /month",
                "Select",
                twelveMonth!!,
                R.drawable.sub_plan_background4
            )
        )

        free!!.add(
            SubPlanChildModel(
                R.drawable.circular_icon,
                "Access to 100+ Free Workout Classes"
            )
        )
        free!!.add(SubPlanChildModel(R.drawable.circular_icon, "Track your Meals"))

        pareantModel!!.add(
            SubPlanParentModel(
                1,
                "FitCru Basic",
                "Free",
                "",
                "Select",
                free!!,
                R.drawable.sub_plan_background1
            )
        )

        val data = pareantModel

        if (num == 1) {
            data!!.removeAt(3)
            data!!.removeAt(0)
        } else if (num == 2) {
            data!!.removeAt(3)
            data!!.removeAt(2)
            data!!.removeAt(1)
        } else if (num == 3)
            data!!.removeAt(3)

        val planParentAdapter: SubPlanParentAdapter =
            SubPlanParentAdapter(data!!, this@SubscriptionPlanActivity, num2)
        subPlanBindnig!!.sPlanRec.layoutManager = LinearLayoutManager(this)
        subPlanBindnig!!.sPlanRec.adapter = planParentAdapter
        planParentAdapter.notifyDataSetChanged()

    }
}
