package com.fitness.fitnessCru.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.HowitWorks
import com.fitness.fitnessCru.databinding.ActivityPlanDetailBinding
import com.fitness.fitnessCru.model.CoachSlabResponse
import com.fitness.fitnessCru.model.Howitworkspojo
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.PlanBreakUpResponse
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.CoachCategoryViewModel
import com.fitness.fitnessCru.vm_factory.CoachCategoryVMFactory

class PlanDetailActivity : AppCompatActivity() {

    private lateinit var repository: Repository
    private lateinit var viewModel: CoachCategoryViewModel
    private lateinit var factory: CoachCategoryVMFactory

    private var coachId = 0
    private var planType = ""
    private var planId = 0
    private var price = ""
    private var catId = 0
    private var discountPrice = 0
    private var productName = ""
    private var duration = ""
    private var planNames = ""
    private var num = ""
    private var binding: ActivityPlanDetailBinding? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_detail)
        binding = ActivityPlanDetailBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.backBtn.setOnClickListener {
            onBackPressed()
        }

        planId = intent.extras?.getInt("planId", 0)!!
        catId = intent.extras?.getInt("catId", 0)!!
        coachId = intent.extras?.getInt("coachId", 0)!!
        planType = intent.extras?.getString("planType", "")!!
        price = intent.extras?.getString("price", "")!!
        productName = intent.extras?.getString("coachName", "")!!
        planNames = intent.extras?.getString("planName", "")!!
        num = intent.extras?.getString("num")!!
        discountPrice = intent.extras?.getInt("discountPrice", 0)!!

        duration = planType[0].toString()

        val shade: Shader = LinearGradient(
            0f,
            0f,
            0f,
            binding!!.monthNo.textSize,
            Color.parseColor("#ffffff"),
            Color.parseColor("#ffffff"),
            Shader.TileMode.CLAMP
        )
        binding!!.monthNo.paint.shader = shade
        //recycleViw1()

        repository = Repository()
        factory = CoachCategoryVMFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(CoachCategoryViewModel::class.java)
        planBreakup()
        recycleViw2()

    }

    private fun callRazorPay(totalPaybleAmount: Int) {

        val convertedAmount = Math.round(totalPaybleAmount.toDouble()).toInt()
        val intent = Intent(this, RazorPaymentActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("amount", convertedAmount)
        bundle.putInt("planId", planId)
        bundle.putInt("coachId", coachId)
        bundle.putInt("catId", catId)
        bundle.putInt("duration", duration.toInt())
        bundle.putString("productName", productName)
        bundle.putString("num", num)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun planBreakup() {
        // val selectCoachAdapter = SelectCoachAdapter(context, planId)
        // selectBinding.selectCoachTypeRV.adapter = selectCoachAdapter

        viewModel.planDetailsBreakup(/*coachId,planType,planId,price*/ coachId,
            "${duration}month_price",
            price.toInt(),
            planId
        )
        viewModel.planBreakupResponse.observe(this) {

            if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0) {
                val data = it.body() as PlanBreakUpResponse

                binding?.apply {
                    planName.text = planNames.drop(9)
                    monthNo.text = planType[0].toString()

                    if (planId == 3 || planId == 2) {
                        totalTv.text = "₹${data.data.actual_price}"
                    } else {
                        totalTv.text = "₹${data.data.offer_price}"
                    }
                    if (data.data.discount_price != null) {
                        discountLl.visibility = View.VISIBLE
                        discountTv.text = "-₹${discountPrice}"
                    } else {
                        discountLl.visibility = View.GONE
                    }

                    gstTv.text = "₹${data.data.gst}"

                    payableTv.text = "₹${data.data.total_payble_amount}"
                    btPay.text = "Pay ₹${data.data.total_payble_amount}"

                    if (data.data.here_what_you_get != null) {
                        tvHowToMakeTitle.visibility = View.VISIBLE
                        tvHowToMake.visibility = View.VISIBLE
                        tvHowToMake.text = data.data.here_what_you_get[0].what_you_get
                        tvHowToMake.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Html.fromHtml(
                                data.data.here_what_you_get[0].what_you_get,
                                Html.FROM_HTML_MODE_COMPACT
                            )
                        } else {
                            Html.fromHtml(data.data.here_what_you_get[0].what_you_get)
                        }
                    } else {
                        tvHowToMakeTitle.visibility = View.GONE
                        tvHowToMake.visibility = View.GONE
                    }

                    btPay.setOnClickListener {
                        callRazorPay(data.data.total_payble_amount)
                    }

                    /*createTagChip(
                            applicationContext,
                            it.toString()
                        )*/
                }

            } else if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 1) {
                Util.toast(
                    this, it.body()!!.message
                )
            } else if (!it.isSuccessful && it.errorBody() != null) Util.toast(
                this,
                Util.error(it.errorBody(), CoachSlabResponse::class.java).message
            )
        }
    }


    /*  private fun recycleViw1() {
          binding!!.rvWhatyouget.layoutManager = LinearLayoutManager(applicationContext)
          val whatyouget: Whatyouget = Whatyouget(applicationContext, data1())
          binding!!.rvWhatyouget.adapter = whatyouget

  *//*        val selectCoachAdapter = Whatyouget(context)
        selectBinding.selectCoachTypeRV.adapter = selectCoachAdapter*//*
    }*/

    /* private fun data1(): ArrayList<String> {
         val list: ArrayList<String> = ArrayList()
         list.add("Customizable Diet Plan")
         list.add("Customizable Workout Routine")
         list.add("Access To 500+ Workout Classes")
         list.add("Track Your Meals")
         list.add("Order Food From Local Healthy Restaurants")
         list.add("Link Fitness Wearables And Track All Data")
         return list
     }*/

    private fun recycleViw2() {
        binding!!.rvHowitworks.layoutManager = LinearLayoutManager(applicationContext)
        val howitWorks: HowitWorks = HowitWorks(applicationContext, data2())
        binding!!.rvHowitworks.adapter = howitWorks
    }

    private fun data2(): ArrayList<Howitworkspojo> {
        val list: ArrayList<Howitworkspojo> = ArrayList()
        list.add(
            Howitworkspojo(
                R.drawable.battle_ropes,
                "Select your Coach"
            )
        )
        list.add(
            Howitworkspojo(
                R.drawable.battle_ropes,
                "Fill out the Pre Consult Questionnaire"
            )
        )

        list.add(
            Howitworkspojo(
                R.drawable.battle_ropes,
                "Get on a consultation with your coach" +
                        " Get started"
            )
        )
        return list
    }

}
