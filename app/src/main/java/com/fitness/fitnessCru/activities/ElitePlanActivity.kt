package com.fitness.fitnessCru.activities

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.HowitWorks
import com.fitness.fitnessCru.adapter.HowtoMake
import com.fitness.fitnessCru.adapter.TraniwithCoach
import com.fitness.fitnessCru.databinding.ActivityElitePlanBinding
import com.fitness.fitnessCru.model.Howitworkspojo
import com.fitness.fitnessCru.model.PlanDetailedHowToMakeRVModel
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.MealTypeResponse
import com.fitness.fitnessCru.response.TrendingOfferResponse
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.TrendingOfferViewModel
import com.fitness.fitnessCru.vm_factory.TrendingOfferVMFactory
import kotlin.math.roundToInt


class ElitePlanActivity : AppCompatActivity() {
    private var binding: ActivityElitePlanBinding? = null
    private lateinit var viewModel: TrendingOfferViewModel
    private lateinit var loading: CustomProgressLoading

    private lateinit var howToMakeAdapter: HowtoMake

    private lateinit var howToMakeModel: ArrayList<PlanDetailedHowToMakeRVModel>

    private var amount = 0.0

    private var programId = 0

    private var productName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityElitePlanBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        programId = intent?.getIntExtra("id", 0)!!
        productName = intent?.getStringExtra("name")!!

        val repository by lazy { Repository() }
        val factory by lazy { TrendingOfferVMFactory(repository) }
        viewModel = ViewModelProvider(this, factory).get(TrendingOfferViewModel::class.java)
        loading = CustomProgressLoading(applicationContext)
        binding!!.gobackbtn.setOnClickListener { onBackPressed() }
        getTrendingOffer()

        howToMakeRV()

        whatYouGetRV()

        recycleViw2()

        hittingViews()

    }

    private fun getTrendingOffer() {
        viewModel.getTrendingOffers(programId)

        loading.showProgress()
        viewModel.response.observe(this) {
            loading.hideProgress()
            if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0 && it.body()!!.data != null) {
                binding?.apply {
                    var data = it.body()!!.data
                    Util.loadImage(applicationContext, imageView, data.image_url)
                    tvTitle.text = data.name
                    tvOfferPrice.text = "₹${data.price}"
                    tvHowToMake.text = data.how_to_make
                    tvMainPrice.text = data.current_price
                    tvMainPrice.setPaintFlags(tvMainPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                    tvWhatYourGet.text = data.what_you_get
                    amount = data.price.toDouble() * 100
                    payBtn.text = "PAY ₹ ${data.price}"
                    recycleTrainwith(data.coaches)
                }

            } else if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 1) {
                Util.toast(
                    applicationContext, it.body()!!.message
                )
            } else if (!it.isSuccessful && it.errorBody() != null) Util.toast(
                applicationContext,
                Util.error(it.errorBody(), MealTypeResponse::class.java).message
            )
        }
    }

    private fun hittingViews() {

        binding!!.payBtn.setOnClickListener {

            val convertedAmount = amount.roundToInt().toInt()

            val intent = Intent(this@ElitePlanActivity, RazorPaymentActivity::class.java)

            val bundle = Bundle()

            bundle.apply {

                putInt("amount", convertedAmount)

                putInt("program", 1)

                putInt("programId", programId)
                bundle.putString("productName", productName)
            }

            intent.putExtras(bundle)

            startActivity(intent)

        }
    }

    private fun howToMakeRV() {

        val linearLayout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding!!.howToMakeRv.apply {
            layoutManager = linearLayout

            setHasFixedSize(true)

            howToMakeModel = setDataInHowToMake()

            howToMakeAdapter = HowtoMake(howToMakeModel, context)

            adapter = howToMakeAdapter
        }
    }

    private fun whatYouGetRV() {

        val linearLayout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding!!.whatYouGetRv.apply {
            layoutManager = linearLayout

            setHasFixedSize(true)

            howToMakeModel = setDataInHowToMake()

            howToMakeAdapter = HowtoMake(howToMakeModel, context)

            adapter = howToMakeAdapter
        }
    }

    private fun setDataInHowToMake(): ArrayList<PlanDetailedHowToMakeRVModel> {
        val make: ArrayList<PlanDetailedHowToMakeRVModel> = ArrayList()
        make.apply {
            add(
                PlanDetailedHowToMakeRVModel(
                    R.drawable.circular_icon,
                    "Customizable Diet Plan"
                )
            )
            add(
                PlanDetailedHowToMakeRVModel(
                    R.drawable.circular_icon,
                    "Customizable Workout Routine"
                )
            )
            add(
                PlanDetailedHowToMakeRVModel(
                    R.drawable.circular_icon,
                    "Immunity Building & Monitoring"
                )
            )
            add(
                PlanDetailedHowToMakeRVModel(
                    R.drawable.circular_icon,
                    "Unlimited Sleep Tracking"
                )
            )
            add(
                PlanDetailedHowToMakeRVModel(
                    R.drawable.circular_icon,
                    "Insights & Weekly Reports"
                )
            )
        }
        return make
    }

    private fun recycleViw2() {
        binding!!.RVhowitworks.layoutManager = LinearLayoutManager(applicationContext)
        val howitWorks = HowitWorks(applicationContext, data2()!!)
        binding!!.RVhowitworks.adapter = howitWorks
    }

    private fun data2(): ArrayList<Howitworkspojo>? {
        val list: ArrayList<Howitworkspojo> = ArrayList()
        list.add(
            Howitworkspojo(
                R.drawable.battle_ropes,
                "In at iaculis lorem. Praesent tempor dictum llus ut molestie. Sed sed an ullamcorper lorem, id cibus odio lorem, id cibus odio…"
            )
        )
        list.add(
            Howitworkspojo(
                R.drawable.battle_ropes,
                "In at iaculis lorem. Praesent tempor dictum llus ut molestie. Sed sed an ullamcorper lorem, id cibus odio lorem, id cibus odio…"
            )
        )
        list.add(
            Howitworkspojo(
                R.drawable.battle_ropes,
                "In at iaculis lorem. Praesent tempor dictum llus ut molestie. Sed sed an ullamcorper lorem, id cibus odio lorem, id cibus odio…"
            )
        )
        return list
    }

    private fun recycleTrainwith(coaches: List<TrendingOfferResponse.Coaches>) {
        binding!!.RVtrainwith.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val traniwithCoach = TraniwithCoach(applicationContext, coaches!!)
        binding!!.RVtrainwith.adapter = traniwithCoach
    }

}