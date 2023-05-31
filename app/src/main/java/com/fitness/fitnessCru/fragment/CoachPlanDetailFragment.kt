package com.fitness.fitnessCru.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.DashboardActivity
import com.fitness.fitnessCru.activities.PlanDetailActivity
import com.fitness.fitnessCru.adapter.HowitWorks
import com.fitness.fitnessCru.adapter.SubPlanParentAdapter2
import com.fitness.fitnessCru.databinding.FragmentCoachPlanDetailBinding
import com.fitness.fitnessCru.model.CoachSlabResponse
import com.fitness.fitnessCru.model.Howitworkspojo
import com.fitness.fitnessCru.model.SubPlanChildModel
import com.fitness.fitnessCru.model.SubPlanParentModel
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.CoachPlansResponse
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.CoachCategoryViewModel
import com.fitness.fitnessCru.vm_factory.CoachCategoryVMFactory

class CoachPlanDetailFragment : Fragment() {

    var pareantModel: ArrayList<SubPlanParentModel>? = null
    var month_: ArrayList<SubPlanChildModel>? = null
    var free: ArrayList<SubPlanChildModel>? = null
    var sixMonth: ArrayList<SubPlanChildModel>? = null
    var twelveMonth: ArrayList<SubPlanChildModel>? = null

    private lateinit var repository: Repository
    private lateinit var viewModel: CoachCategoryViewModel
    private lateinit var factory: CoachCategoryVMFactory

    private lateinit var binding: FragmentCoachPlanDetailBinding

    private var planId = 0
    private var catId = 0
    private var coachId = 0
    private var coachName = ""
    private var num = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCoachPlanDetailBinding.inflate(inflater, container, false)
        binding.apply {
            gobackbtn.setOnClickListener {
                requireActivity().onBackPressed()
            }
            skipTv.setOnClickListener {
                val gotoDashBoard = Intent(requireActivity(), DashboardActivity::class.java)
                startActivity(gotoDashBoard)
            }
        }

        planId = requireArguments().getInt("planId", 0)
        catId = requireArguments().getInt("catId", 0)
        coachId = requireArguments().getInt("coachId", 0)
        coachName = requireArguments().getString("coachName").toString()
        num = requireArguments().getString("num").toString()

        Log.v("numcoachplan", num)
        binding.coachNameTv.text = coachName

        repository = Repository()
        factory = CoachCategoryVMFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(CoachCategoryViewModel::class.java)

        if (planId == 3) {
            viewModel.coachPlan(coachId)
            viewModel.coachPlan.observe(requireActivity()) {
                if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0) {
                    getAllPlans(it.body()!!.data)
                    Log.v("coachtyupe", it.body()!!.data.toString())
                } else if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 1) {
                    Util.toast(
                        requireContext(), it.body()!!.message
                    )
                } else if (!it.isSuccessful && it.errorBody() != null) Util.toast(
                    requireContext(),
                    Util.error(it.errorBody(), CoachSlabResponse::class.java).message
                )
            }
        } else {
            getAllPlans(null)
        }

        recycleViw2()
        openFAQ()
        return binding.root
    }

    private fun getAllPlans(data: CoachPlansResponse.Data?) {
        pareantModel = ArrayList()
        free = ArrayList()
        month_ = ArrayList()
        sixMonth = ArrayList()
        twelveMonth = ArrayList()

        Log.v("PlanId", planId.toString())

        free!!.add(
            SubPlanChildModel(
                R.drawable.circular_icon,
                "Get a complete assessment along with a short term program which is customised for you to kick start your journey"
            )
        )

        pareantModel!!.add(
            SubPlanParentModel(
                1,
                null,
                "1 Month  Assessment Plan",
                "1month_price",
                if (planId == 3) {
                    if (data?.onemonth_price!!.offer_price > 0) "₹${data?.onemonth_price!!.offer_price}" else "₹${data?.onemonth_price!!.actual_price}"
                } else "₹75000",
                free!!,
                R.drawable.sub_plan_background1,
                if (planId == 3) data?.onemonth_price else null
            )
        )


        month_!!.add(
            SubPlanChildModel(
                R.drawable.circular_icon,
                "Get completely transformed.Invest the next 3 months to hit your fitness goals"
            )
        )

        pareantModel!!.add(
            SubPlanParentModel(
                3,
                null,
                "3 Months Transformation Plan",
                "3month_price",
                if (planId == 3) {
                    if (data?.threemonth_price!!.offer_price > 0) "₹${data?.threemonth_price!!.offer_price}" else "₹${data?.threemonth_price!!.actual_price}"
                } else "₹200000",
                month_!!,
                R.drawable.sub_plan_background2,
                if (planId == 3) data?.threemonth_price else null
            )
        )

        sixMonth!!.add(
            SubPlanChildModel(
                R.drawable.circular_icon,
                "Invest 6 months to change your life in a sustainable way with a holistic approach"
            )
        )

        pareantModel!!.add(
            SubPlanParentModel(
                6,
                null,
                "6 Months Lifestyle Plan",
                "6month_price",
                if (planId == 3) {
                    if (data?.sixmonth_price!!.offer_price > 0) "₹${data?.sixmonth_price!!.offer_price}" else "₹${data?.sixmonth_price!!.actual_price}"
                } else "₹350000",
                sixMonth!!,
                R.drawable.sub_plan_background3,
                if (planId == 3) data?.sixmonth_price else null
            )
        )

        val list: MutableList<SubPlanParentModel> = pareantModel!!.toMutableList()
        list.reverse()
        val planParentAdapter =
            SubPlanParentAdapter2(list!!, requireContext(), object :
                SubPlanParentAdapter2.SelectedMonthsPlans {
                override fun onClick(
                    price: String,
                    month: String,
                    planName: String,
                    discountPrice: Int
                ) {
                    val nPrice = price.drop(1)
                    Log.v("price", nPrice)
                    val intent = Intent(context, PlanDetailActivity::class.java)
                    intent.putExtra("planId", planId)
                    intent.putExtra("catId", catId)
                    intent.putExtra("coachId", coachId)
                    intent.putExtra("price", nPrice)
                    intent.putExtra("planType", month)
                    intent.putExtra("coachName", coachName)
                    intent.putExtra("planName", planName)
                    intent.putExtra("discountPrice", discountPrice)
                    intent.putExtra("num", num)
                    startActivity(intent)
                }
            })
        binding!!.sPlanRec.layoutManager = LinearLayoutManager(requireContext())
        binding!!.sPlanRec.adapter = planParentAdapter
        planParentAdapter.notifyDataSetChanged()

    }

    private fun recycleViw2() {
        binding!!.rvHowitworks.layoutManager = LinearLayoutManager(requireContext())
        val howitWorks: HowitWorks = HowitWorks(requireContext(), data2())
        binding!!.rvHowitworks.adapter = howitWorks
    }

    private fun data2(): ArrayList<Howitworkspojo> {
        val list: ArrayList<Howitworkspojo> = ArrayList()
        list.add(
            Howitworkspojo(
                R.drawable.battle_ropes,
                "Select your Coach "
            )
        )
        list.add(
            Howitworkspojo(
                R.drawable.battle_ropes,
                "Fill out the Pre Consult Questionare"
            )
        )
        list.add(
            Howitworkspojo(
                R.drawable.battle_ropes,
                "Get on a consultation with your coach Get started"
            )
        )
        return list
    }


    private fun openFAQ() {
        binding.apply {

            questionOneContainer.setOnClickListener {
                visibilityQA(
                    binding.questionOne,
                    binding.answerOneContainer
                )
            }

            question2Container.setOnClickListener {
                visibilityQA(binding.question2, binding.answer2Container)
            }

            question3Container.setOnClickListener {
                visibilityQA(binding.question3, binding.answer3Container)
            }

            question4Container.setOnClickListener {
                visibilityQA(binding.question4, binding.answer4Container)
            }

            question5Container.setOnClickListener {
                visibilityQA(binding.question5, binding.answer5Container)
            }

            question6Container.setOnClickListener {
                visibilityQA(binding.question6, binding.answer6Container)
            }

            question7Container.setOnClickListener {
                visibilityQA(binding.question7, binding.answer7Container)
            }

            question8Container.setOnClickListener {
                visibilityQA(binding.question8, binding.answer8Container)
            }

            question9Container.setOnClickListener {
                visibilityQA(binding.question9, binding.answer9Container)
            }

            question10Container.setOnClickListener {
                visibilityQA(
                    binding.question10,
                    binding.answer10Container
                )
            }

            question11Container.setOnClickListener {
                visibilityQA(
                    binding.question11,
                    binding.answer11Container
                )
            }

            question12Container.setOnClickListener {
                visibilityQA(
                    binding.question12,
                    binding.answer12Container
                )
            }

            question13Container.setOnClickListener {
                visibilityQA(
                    binding.question13,
                    binding.answer13Container
                )
            }

            question14Container.setOnClickListener {
                visibilityQA(
                    binding.question14,
                    binding.answer14Container
                )
            }

            question15Container.setOnClickListener {
                visibilityQA(
                    binding.question15,
                    binding.answer15Container
                )
            }

            question16Container.setOnClickListener {
                visibilityQA(
                    binding.question16,
                    binding.answer16Container
                )
            }

            question17Container.setOnClickListener {
                visibilityQA(
                    binding.question17,
                    binding.answer17Container
                )
            }

            question18Container.setOnClickListener {
                visibilityQA(
                    binding.question18,
                    binding.answer18Container
                )
            }

            question19Container.setOnClickListener {
                visibilityQA(
                    binding.question19,
                    binding.answer19Container
                )
            }
        }
    }

    private fun visibilityQA(textView: TextView, linearLayout: LinearLayout) {
        linearLayout.visibility =
            if (linearLayout.visibility == View.GONE) View.VISIBLE else View.GONE
        textView.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            if (linearLayout.visibility == View.GONE) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up,
            0
        )
    }
}