package com.fitness.fitnessCru.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.adapter.CGetCoachAdapter
import com.fitness.fitnessCru.adapter.CoachHowItWorksAdapter
import com.fitness.fitnessCru.adapter.CoachInfoAdapter
import com.fitness.fitnessCru.databinding.FragmentCMentalHealthBinding
import com.fitness.fitnessCru.interfaces.CoachPlanClickInterface
import com.fitness.fitnessCru.model.CGetCoachModel
import com.fitness.fitnessCru.model.CoachBundle
import com.fitness.fitnessCru.model.CoachHowItWorksModel
import com.fitness.fitnessCru.model.CoachInfoModel
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.Constants
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.CoachCategoryViewModel
import com.fitness.fitnessCru.vm_factory.CoachCategoryVMFactory

class CMentalHealthFragment(val cat_id: Int, val tab_name: String) : Fragment() {

    private lateinit var mentalHealthBinding: FragmentCMentalHealthBinding

    private lateinit var coachInfoAdapter: CoachInfoAdapter

    private lateinit var coachInfoModel: ArrayList<CoachInfoModel>

    private lateinit var howItWorksAdapter: CoachHowItWorksAdapter

    private lateinit var howItWorksModel: ArrayList<CoachHowItWorksModel>

    private lateinit var getCoachAdapter: CGetCoachAdapter

    val slabId = 0
    val slabType = ""
//    private lateinit var mentalHealthCoachModel: ArrayList<MentalHealthCoachModel>
//
//    private lateinit var mentalHealthCoachAdapter: MentalHealthCoachAdapter

    private lateinit var repository: Repository
    private lateinit var viewModel: CoachCategoryViewModel
    private lateinit var factory: CoachCategoryVMFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mentalHealthBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_c_mental_health, container, false)

        repository = Repository()

        factory = CoachCategoryVMFactory(repository)

        viewModel = ViewModelProvider(this, factory)[CoachCategoryViewModel::class.java]

        mentalHealthBinding.coachTitleTv.text = "$tab_name"

        setCoachInfoRV()
        setHowItWoksRV()
        openFAQ()
        setGetCoachRV()
        //       setMentalHealthRV()
        mentalHealthBinding.sendMessage.setOnClickListener {
            Util.sendMessage(
                context?.applicationContext!!,
                "Hello"
            )
        }
        return mentalHealthBinding.root
    }

    private fun setGetCoachRV() {

        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mentalHealthBinding.getCoachRv.layoutManager = linearLayout

        mentalHealthBinding.getCoachRv.setHasFixedSize(true)

        getCoachAdapter = CGetCoachAdapter(object : CoachPlanClickInterface {
            override fun onCoachClick(
                key: String,
                price: String,
                catId: Int,
                feeId: Int,
                slabTypeId: String,
                duration: String
            ) {
                val intent = Intent(context!!.applicationContext, SetupAllActivity::class.java)
                intent.putExtra(Constants.DESTINATION, Constants.COACHING)
                intent.putExtra(
                    "data",
                    CoachBundle(price, key, catId, tab_name, slabId, slabType, duration, feeId)
                )
                context!!.startActivity(intent)
            }
        })

        getCoachAdapter.setList(setDataInGetCoachList())
        mentalHealthBinding.getCoachRv.adapter = getCoachAdapter
    }

    private fun setDataInGetCoachList(): ArrayList<CGetCoachModel> {
        val getCoach: ArrayList<CGetCoachModel> = ArrayList()

        viewModel.getSubscriptionPlanMentalHealth(cat_id)
        viewModel.subPlanMentalHealthResponse.observe(viewLifecycleOwner) {

            if (it.isSuccessful && it.code() == 200 && it.body() != null && it.body()!!.data != null) {
                val data = it.body()!!.data

                getCoach.clear()
                getCoach.apply {
                    add(
                        CGetCoachModel(
                            "1 Session",
                            data.coach_fee_structure.actual_price_one_months ?: "",
                            data.coach_category_id,
                            data.id,
                            data.coach_slab_type ?: "",
                            "current_price_one_months",
                            data.current_price.current_price_one_months
                        )
                    )
                    add(
                        CGetCoachModel(
                            "4 Sessions",
                            data.coach_fee_structure.actual_price_three_months ?: "",
                            data.coach_category_id,
                            data.id,
                            data.coach_slab_type ?: "",
                            "current_price_three_months",
                            data.current_price.current_price_three_months
                        )
                    )
                    add(
                        CGetCoachModel(
                            "12 Sessions",
                            data.coach_fee_structure.actual_price_six_months ?: "",
                            data.coach_category_id,
                            data.id,
                            data.coach_slab_type ?: "",
                            "current_price_six_months",
                            data.current_price.current_price_six_months
                        )
                    )
                }
                getCoachAdapter.notifyDataSetChanged()
            }

        }
        return getCoach
    }


//    private fun setMentalHealthRV() {
//
//        val gridLayout = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
//
//        mentalHealthBinding.mentalCoachSlabRv.layoutManager = gridLayout
//
//        mentalHealthBinding.mentalCoachSlabRv.setHasFixedSize(true)
//
//        mentalHealthCoachModel = setDataInMentalHealthList()
//
//        mentalHealthCoachAdapter = MentalHealthCoachAdapter(mentalHealthCoachModel, context)
//
//        mentalHealthBinding.mentalCoachSlabRv.adapter = mentalHealthCoachAdapter
//
//    }

    private fun setCoachInfoRV() {
        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mentalHealthBinding.coachInfoRv.layoutManager = linearLayout
        mentalHealthBinding.coachInfoRv.setHasFixedSize(true)
        coachInfoModel = setDataInCoachInfoList()
        coachInfoAdapter = CoachInfoAdapter(coachInfoModel, context)
        mentalHealthBinding.coachInfoRv.adapter = coachInfoAdapter
    }

    private fun setDataInCoachInfoList(): ArrayList<CoachInfoModel> {
        val info: ArrayList<CoachInfoModel> = ArrayList()
        info.apply {
            add(
                CoachInfoModel(
                    R.drawable.coach_certified_icon,
                    "Certified Coaches"
                )
            )
            add(
                CoachInfoModel(
                    R.drawable.coach_certified_icon,
                    "Personalized diet & workout plans for your lifestyle, Goals & food preferences"
                )
            )
            add(
                CoachInfoModel(
                    R.drawable.coach_certified_icon,
                    "Daily Monitoring with coach to understand your progress"
                )
            )
            add(
                CoachInfoModel(
                    R.drawable.coach_certified_icon,
                    "Continuous Support on Chat"
                )
            )

        }

        return info
    }

    private fun setHowItWoksRV() {

        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mentalHealthBinding.coachHowItWorksRv.layoutManager = linearLayout

        mentalHealthBinding.coachHowItWorksRv.setHasFixedSize(true)

        howItWorksModel = setDataInHowItWoksList()

        howItWorksAdapter = CoachHowItWorksAdapter(howItWorksModel, context)

        mentalHealthBinding.coachHowItWorksRv.adapter = howItWorksAdapter
    }

    private fun setDataInHowItWoksList(): ArrayList<CoachHowItWorksModel> {
        val works: ArrayList<CoachHowItWorksModel> = ArrayList()
        works.apply {
            add(
                CoachHowItWorksModel(
                    R.drawable.icon_for_coach,
                    "In at iaculis lorem. Praesent tempor dictum llus ut molestie. Sed sed an ullamcorper lorem, id cibus odio lorem, id cibus odio…",
                )
            )
            add(
                CoachHowItWorksModel(
                    R.drawable.icon_for_coach,
                    "In at iaculis lorem. Praesent tempor dictum llus ut molestie. Sed sed an ullamcorper lorem, id cibus odio lorem, id cibus odio…",
                )
            )
            add(
                CoachHowItWorksModel(
                    R.drawable.icon_for_coach,
                    "In at iaculis lorem. Praesent tempor dictum llus ut molestie. Sed sed an ullamcorper lorem, id cibus odio lorem, id cibus odio…",
                )
            )
            add(
                CoachHowItWorksModel(
                    R.drawable.icon_for_coach,
                    "In at iaculis lorem. Praesent tempor dictum llus ut molestie. Sed sed an ullamcorper lorem, id cibus odio lorem, id cibus odio…",
                )
            )
        }

        return works
    }

    private fun openFAQ() {
        mentalHealthBinding.apply {

            questionOneContainer.setOnClickListener {
                visibilityQA(
                    questionOne,
                    answerOneContainer
                )
            }

            question2Container.setOnClickListener {
                visibilityQA(question2, answer2Container)
            }

            question3Container.setOnClickListener {
                visibilityQA(question3, answer3Container)
            }

            question4Container.setOnClickListener {
                visibilityQA(question4, answer4Container)
            }

            question5Container.setOnClickListener {
                visibilityQA(question5, answer5Container)
            }

            question6Container.setOnClickListener {
                visibilityQA(question6, answer6Container)
            }

            question7Container.setOnClickListener {
                visibilityQA(question7, answer7Container)
            }

            question8Container.setOnClickListener {
                visibilityQA(question8, answer8Container)
            }

            question9Container.setOnClickListener {
                visibilityQA(question9, answer9Container)
            }

            question10Container.setOnClickListener {
                visibilityQA(
                    question10,
                    answer10Container
                )
            }

            question11Container.setOnClickListener {
                visibilityQA(
                    question11,
                    answer11Container
                )
            }

            question12Container.setOnClickListener {
                visibilityQA(
                    question12,
                    answer12Container
                )
            }

            question13Container.setOnClickListener {
                visibilityQA(
                    question13,
                    answer13Container
                )
            }

            question14Container.setOnClickListener {
                visibilityQA(
                    question14,
                    answer14Container
                )
            }

            question15Container.setOnClickListener {
                visibilityQA(
                    question15,
                    answer15Container
                )
            }

            question16Container.setOnClickListener {
                visibilityQA(
                    question16,
                    answer16Container
                )
            }

            question17Container.setOnClickListener {
                visibilityQA(
                    question17,
                    answer17Container
                )
            }

            question18Container.setOnClickListener {
                visibilityQA(
                    question18,
                    answer18Container
                )
            }

            question19Container.setOnClickListener {
                visibilityQA(
                    question19,
                    answer19Container
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