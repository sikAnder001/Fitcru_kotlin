package com.fitness.fitnessCru.activities

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.MealDetailAdapter
import com.fitness.fitnessCru.body.ConsumedAllMealBody
import com.fitness.fitnessCru.body.ConsumedMealBody
import com.fitness.fitnessCru.databinding.ActivityMealDetailBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.ConsumedMealResponse
import com.fitness.fitnessCru.response.MealTypeDetailResponse
import com.fitness.fitnessCru.response.MealTypeResponse
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.DashBoardViewModel
import com.fitness.fitnessCru.vm_factory.DashBoardVMFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class MealDetailActivity : AppCompatActivity() {

    private var binding: ActivityMealDetailBinding? = null
    private var meal_type_id: Int = 0
    private var date: String = ""
    private var time: String = ""

    private lateinit var loading: CustomProgressLoading

    private lateinit var repository: Repository
    private lateinit var viewModel: DashBoardViewModel
    private lateinit var factory: DashBoardVMFactory

    private lateinit var data: MealTypeDetailResponse.Data

    private lateinit var foodsList: ArrayList<ConsumedAllMealBody.Foods>


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMealDetailBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        meal_type_id = intent.extras?.getInt("data", 0)!!
        date = intent.extras?.getString("date")!!
        time = intent.extras?.getString("time")!!

        loading = CustomProgressLoading(this)

        repository = Repository()

        factory = DashBoardVMFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(DashBoardViewModel::class.java)

        /* val balloon: Balloon = Balloon.Builder(applicationContext)
             .setArrowSize(10)
             .setArrowOrientation(ArrowOrientation.TOP)
             .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
             .setArrowPosition(0.3f)
             .setWidth(WRAP)
             .setHeight(WRAP)
             .setTextSize(15f)
             .setPadding(10)
             .setMarginRight(12)
             .setCornerRadius(6f)
             .setAlpha(0.9f)
             .setText("Mark All as Complete.")
             .setTextColor(ContextCompat.getColor(applicationContext, R.color.offwhite))
             .setTextIsHtml(true)
             .setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey_70))
             .setBalloonAnimation(BalloonAnimation.FADE)
             .build()

         balloon.showAlignTop(binding!!.checkbox)*/


        setRecyclerViewFoodInit()
        observer()
        goBack()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun observer() {
        try {
            viewModel.mealdetail.observe(this) {
                loading.hideProgress()
                if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0 && it.body()!!.data != null) {
                    data = it.body()!!.data

//                Toast.makeText(applicationContext, "get meal detail", Toast.LENGTH_SHORT).show()
                    getDetail(data)

                } else if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 1) Util.toast(
                    applicationContext,
                    it.body()!!.message
                ) else if (!it.isSuccessful && it.errorBody() != null) Util.toast(
                    applicationContext,
                    Util.error(it.errorBody(), MealTypeResponse::class.java).message
                )
            }
        } catch (e: Exception) {
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun setRecyclerViewFoodInit() {
        loading.showProgress()
        viewModel.getMealDetail(meal_type_id, date, time)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getDetail(data: MealTypeDetailResponse.Data) {

        binding.apply {
            try {
                Glide.with(applicationContext).load(data.food_data[0].food.image_url)
                    .placeholder(R.drawable.place_holder)
                    .into(this!!.iv)

                mealtypeTV?.text = data.food_data[0].meal_type.mealtype


                for (i in data.food_data) {
                    if (i.is_complete == "0") {
                        checkbox.isChecked = false
                        Log.v("work3", i.is_complete)
                        markAll.text = "Mark all"
                        break
                    } else {
                        checkbox.isChecked = true
                        markAll.text = "Unmark all"
                    }
                }

                foodsList = ArrayList()

                checkbox.setOnClickListener {
                    if (checkbox.isChecked) {
                        foodsList.clear()
                        for (element in data.food_data) {
                            foodsList.add(
                                ConsumedAllMealBody.Foods(
                                    element.id, element.food_id,
                                    element.time,
                                    element.date
                                )
                            )
                        }

                        var bodyB = ConsumedAllMealBody(foodsList)
                        consumeAll(bodyB)

                    } else {
                        val ids = StringJoiner(",")
                        for (element in data.food_data) {
                            if (element.consumed_meal_id != 0) {
                                ids.add(element.consumed_meal_id.toString())
                            }
                        }

                        Log.v("Now", ids.toString())
                        removeConsumeAll(ids.toString())
                    }
                }



                binding!!.rvMealDetail.adapter =
                    MealDetailAdapter(
                        this@MealDetailActivity,
                        data.food_data,
                        object : MealDetailAdapter.AvailabilityInterfaceTwo {
                            @RequiresApi(Build.VERSION_CODES.O)
                            override fun onClick(
                                mealTypeId: Int,
                                food_id: Int,
                                date: String,
                                time: String,
                                consumed_id: Int,
                                id: Int
                            ) {
                                if (id == 0) {
                                    mealConsumed(mealTypeId, food_id, date, time)
                                } else {
                                    mealNotConsumed(consumed_id)
                                }

                            }
                        }
                    )
                binding!!.rvMealDetail.adapter!!.notifyDataSetChanged()
                try {
                    val carbPro =
                        ((data.benefits.consume_carbs * 100) / data.benefits.carbs).toFloat()
                    progressCarb.progress = carbPro

/*
                    val totalCarbGram = (data.benefits.carbs * 0.129598).toInt()
                    val consumeCarbGram = (data.benefits.consume_carbs * 0.129598).toInt() */
                    val leftCarb = (data.benefits.carbs - data.benefits.consume_carbs)
                    conCarbGTv.text = ("${leftCarb.toInt()}g")
                    totalCarbGTv.text = ("of ${data.benefits.carbs.toInt()}g left")
                } catch (e: Exception) {
                    progressCarb.progress = 0f
                    conCarbGTv.text = ("0g")
                    totalCarbGTv.text = ("of 0g left")

                }
                try {
                    val proteinPro =
                        ((data.benefits.consume_protein * 100) / data.benefits.protein).toFloat()
                    progressProtein.progress = proteinPro

                    /* val totalProGram = (data.benefits.protein * 0.129598).toInt()
                     val consumeProGram = (data.benefits.consume_protein * 0.129598).toInt() */
                    val leftPro = (data.benefits.protein - data.benefits.consume_protein)
                    consumeProGTv.text = ("${leftPro.toInt()}g")
                    totalProGTv.text = ("of ${data.benefits.protein.toInt()}g left")

                } catch (e: Exception) {
                    progressProtein.progress = 0f
                    consumeProGTv.text = ("0g")
                    totalProGTv.text = ("of 0g left")
                }
                try {
                    val fatPro = ((data.benefits.consume_fat * 100) / data.benefits.fat).toFloat()
                    progressFat.progress = fatPro

                    /* val totalFatGram = (data.benefits.fat * 0.129598).toInt()
                     val consumeFatGram = (data.benefits.consume_fat * 0.129598).toInt() */
                    val leftFat = (data.benefits.fat - data.benefits.consume_fat)
                    consumeFatGtV.text = ("${leftFat.toInt()}g")
                    totalFatGTv.text = ("of ${data.benefits.fat.toInt()}g left")

                } catch (e: Exception) {
                    progressFat.progress = 0f
                    consumeFatGtV.text = ("0g")
                    totalFatGTv.text = ("of 0g left")

                }

            } catch (e: Exception) {
            }
        }
    }

    private fun removeConsumeAll(ids: String) {

        viewModel.removeAllConsume(ids)
        viewModel.uncheckConsumeAll.observe(this@MealDetailActivity) { response ->
            loading.hideProgress()
            if (response.isSuccessful && response.body() != null) {
                overridePendingTransition(0, 0);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
//                viewModel.getMealDetail(meal_type_id, this.date, this.time)
//                binding!!.rvMealDetail.adapter!!.notifyDataSetChanged()
            } else if (!response.isSuccessful && response.errorBody() != null)
                Util.error(response.errorBody(), ConsumedMealResponse::class.java).message
            else Util.toast(applicationContext, "Something went wrong")
        }
    }

    private fun consumeAll(bodyB: ConsumedAllMealBody) {
//        loading.showProgress()
        viewModel.consumedAllMeal(bodyB)
        viewModel.consumeAll.observe(this@MealDetailActivity) { response ->
            loading.hideProgress()
            if (response.isSuccessful && response.body() != null) {
                overridePendingTransition(0, 0);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
//                viewModel.getMealDetail(meal_type_id, this.date, this.time)
//                binding!!.rvMealDetail.adapter!!.notifyDataSetChanged()
            } else if (!response.isSuccessful && response.errorBody() != null)
                Util.error(response.errorBody(), ConsumedMealResponse::class.java).message
            else Util.toast(applicationContext, "Something went wrong")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mealNotConsumed(consume_meal_id: Int) {
        if (consume_meal_id != null) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd,hh:mm a", Locale.ENGLISH)
            val formatted = current.format(formatter)


            viewModel.notConsumedMeal(consume_meal_id)
            viewModel.notConsumed.observe(this) { response ->
                loading.hideProgress()
                if (response.isSuccessful && response.body() != null) {
                    response.body()!!.message
                    overridePendingTransition(0, 0);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
//                    finish();
//                    overridePendingTransition(0, 0);
//                    startActivity(intent);
//                    overridePendingTransition(0, 0);
//                    viewModel.getMealDetail(meal_type_id, this.date, this.time)
//                    binding!!.rvMealDetail.adapter!!.notifyDataSetChanged()
                } else if (!response.isSuccessful && response.errorBody() != null)
                    Util.error(response.errorBody(), ConsumedMealResponse::class.java).message
                else Util.toast(applicationContext, "Something went wrong")
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mealConsumed(mealTypeid: Int, food_id: Int, date: String, time: String) {
        if (mealTypeid != null && food_id != null) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd,hh:mm a", Locale.ENGLISH)
            val formatted = current.format(formatter)

//            loading.showProgress()
            viewModel.consumedMeal(
                ConsumedMealBody(
                    mealTypeid, food_id,
                    time,
                    date
                )
            )

            viewModel.response1.observe(this) { response ->
                loading.hideProgress()
                if (response.isSuccessful && response.body() != null) {
                    overridePendingTransition(0, 0);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    //                    finish();
//                    overridePendingTransition(0, 0);
//                    startActivity(intent);
//                    overridePendingTransition(0, 0);
//                    response.body()!!.message
//                    viewModel.getMealDetail(meal_type_id, this.date, this.time)
//                    binding!!.rvMealDetail.adapter!!.notifyDataSetChanged()
                } else if (!response.isSuccessful && response.errorBody() != null)
                    Util.error(response.errorBody(), ConsumedMealResponse::class.java).message
                else Util.toast(applicationContext, "Something went wrong")
            }
        }
    }

    private fun goBack() {
        binding!!.gobackbtn.setOnClickListener { onBackPressed() }
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
////        if (intent.extras?.getString("back_location") == "dashboard") {
////            startActivity(Intent(applicationContext, DashboardActivity::class.java))
////            finish()
////        } else {
////            intent.putExtra("locate","3")
////            var intent = Intent(applicationContext, DashboardActivity::class.java)
////            var bundle = Bundle()
////            bundle.putString("locate","3")
////            intent.putExtras(bundle)
////            startActivity(intent)
////            finish()
////        }
//    }
}
