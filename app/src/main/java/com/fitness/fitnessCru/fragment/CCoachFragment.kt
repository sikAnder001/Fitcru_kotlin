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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.adapter.*
import com.fitness.fitnessCru.databinding.FragmentCCoachBinding
import com.fitness.fitnessCru.interfaces.CoachClickInterface
import com.fitness.fitnessCru.interfaces.CoachPlanClickInterface
import com.fitness.fitnessCru.model.*
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.Constants
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.utility.Util.Companion.sendMessage
import com.fitness.fitnessCru.viewmodel.CoachCategoryViewModel
import com.fitness.fitnessCru.vm_factory.CoachCategoryVMFactory

class CCoachFragment(val cat_id: Int, val tab_name: String) : Fragment(),
    CoachCategoryAdapter.CoachCategoryOnClickListener {

    private lateinit var coachingCoachBinding: FragmentCCoachBinding

    private lateinit var coachInfoAdapter: CoachInfoAdapter

    private lateinit var coachInfoModel: ArrayList<CoachInfoModel>

    private lateinit var getCoachAdapter: CGetCoachAdapter

    private lateinit var howItWorksAdapter: CoachHowItWorksAdapter

    private lateinit var howItWorksModel: ArrayList<CoachHowItWorksModel>

    private lateinit var coachCategoryAdapter: CoachCategoryAdapter

    private lateinit var coachCategoryModel: ArrayList<CoachCategoryModel>

    private lateinit var coachSlabAdapter: CoachSlabAdapter

    private lateinit var coachSlabModel: ArrayList<CoachSlabModel>

    private lateinit var repository: Repository

    private var slabId = 0

    private var slabType = ""

    private lateinit var viewModel: CoachCategoryViewModel

    private lateinit var factory: CoachCategoryVMFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        coachingCoachBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_c_coach, container, false)

        repository = Repository()

        factory = CoachCategoryVMFactory(repository)

        viewModel = ViewModelProvider(this, factory)[CoachCategoryViewModel::class.java]

        setCoachInfoRV()

        setHowItWoksRV()

        setCoachCategoryRV()

        setCoachSlabRV()

        openFAQ()

        coachingCoachBinding.sendMessage.setOnClickListener {
            sendMessage(
                context?.applicationContext!!,
                "Hello"
            )
        }

        return coachingCoachBinding.root
    }

    private fun setCoachInfoRV() {
        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        coachingCoachBinding.coachInfoRv.layoutManager = linearLayout
        coachingCoachBinding.coachInfoRv.setHasFixedSize(true)
        coachInfoModel = setDataInCoachInfoList()
        coachInfoAdapter = CoachInfoAdapter(coachInfoModel, context)
        coachingCoachBinding.coachInfoRv.adapter = coachInfoAdapter
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

    private fun setGetCoachRV() {

        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        coachingCoachBinding.getCoachRv.layoutManager = linearLayout

        coachingCoachBinding.getCoachRv.setHasFixedSize(true)

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
        coachingCoachBinding.getCoachRv.adapter = getCoachAdapter
    }

    private fun setDataInGetCoachList(): ArrayList<CGetCoachModel> {
        val getCoach: ArrayList<CGetCoachModel> = ArrayList()
        viewModel.getSubscriptionPlan(cat_id, slabId)
        viewModel.subPlanResponse.observe(viewLifecycleOwner) {

            if (it.isSuccessful && it.code() == 200 && it.body() != null && it.body()!!.data != null) {
                val data = it.body()!!.data

                getCoach.clear()
                getCoach.apply {
                    add(
                        CGetCoachModel(
                            "1 Month",
                            data.coach_fee_structure.actual_price_one_months,
                            data.coach_category_id,
                            data.id,
                            data.coach_slab_type,
                            "current_price_one_months",
                            data.current_price.current_price_one_months
                        )
                    )
                    add(
                        CGetCoachModel(
                            "3 Month",
                            data.coach_fee_structure.actual_price_three_months,
                            data.coach_category_id,
                            data.id,
                            data.coach_slab_type,
                            "current_price_three_months",
                            data.current_price.current_price_three_months
                        )
                    )
                    add(
                        CGetCoachModel(
                            "6 Month",
                            data.coach_fee_structure.actual_price_six_months,
                            data.coach_category_id,
                            data.id,
                            data.coach_slab_type,
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

    private fun setHowItWoksRV() {

        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        coachingCoachBinding.coachHowItWorksRv.layoutManager = linearLayout

        coachingCoachBinding.coachHowItWorksRv.setHasFixedSize(true)

        howItWorksModel = setDataInHowItWoksList()

        howItWorksAdapter = CoachHowItWorksAdapter(howItWorksModel, context)

        coachingCoachBinding.coachHowItWorksRv.adapter = howItWorksAdapter
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

    private fun setCoachCategoryRV() {

        val linearLayout = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)

        coachingCoachBinding.coachCategoryRv.layoutManager = linearLayout

        coachingCoachBinding.coachCategoryRv.setHasFixedSize(true)

        coachCategoryModel = setDataInCoachCategoryList()

        coachCategoryAdapter = CoachCategoryAdapter(coachCategoryModel, context, this)

        coachingCoachBinding.coachCategoryRv.adapter = coachCategoryAdapter
    }

    private fun setDataInCoachCategoryList(): ArrayList<CoachCategoryModel> {
        val coach: ArrayList<CoachCategoryModel> = ArrayList()
        coach.apply {
            add(
                CoachCategoryModel(
                    R.drawable.personal_trainer_icon,
                    "Fitness"
                )
            )

            add(
                CoachCategoryModel(
                    R.drawable.personal_trainer_icon,
                    "Nutritionist"
                )
            )

            add(
                CoachCategoryModel(
                    R.drawable.personal_trainer_icon,
                    "Mental Health Coach"
                )
            )
        }

        return coach
    }

    private fun setCoachSlabRV() {

        val linearLayout = GridLayoutManager(context, 4, LinearLayoutManager.VERTICAL, false)

        coachingCoachBinding.coachSlabRv.layoutManager = linearLayout

        coachSlabModel = setDataInCoachSlabList()

        coachSlabAdapter = CoachSlabAdapter(requireContext(), object : CoachClickInterface {
            override fun onCoachClick(id: Int, name: String) {
                slabId = id
                slabType = name
                setGetCoachRV()
                coachingCoachBinding.coachTitleTv.text = "$tab_name"
            }
        })

        coachingCoachBinding.coachSlabRv.adapter = coachSlabAdapter

        viewModel.getCoachSlab()
        viewModel.response1.observe(viewLifecycleOwner) {

            if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0) {

                coachSlabAdapter.setList(it.body()!!.data)

            } else if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 1) {
                Util.toast(
                    requireContext(), it.body()!!.message
                )
                activity?.onBackPressed()
            } else if (!it.isSuccessful && it.errorBody() != null) Util.toast(
                requireContext(),
                Util.error(it.errorBody(), CoachSlabResponse::class.java).message
            )
        }
    }

    private fun setDataInCoachSlabList(): ArrayList<CoachSlabModel> {
        val slab: ArrayList<CoachSlabModel> = ArrayList()
        slab.apply {

            add(
                CoachSlabModel(
                    R.drawable.personal_trainer_icon,
                    "Elite coach"
                )
            )
            add(
                CoachSlabModel(
                    R.drawable.personal_trainer_icon,
                    "Pro coach"
                )
            )
            add(
                CoachSlabModel(
                    R.drawable.personal_trainer_icon,
                    "Super coach"
                )
            )
            add(
                CoachSlabModel(
                    R.drawable.personal_trainer_icon,
                    "Legendary coach"
                )
            )
        }

        return slab
    }

    override fun onClick(position: Int) {

        /* coachingCoachBinding.coachSlabRv.visibility = View.VISIBLE*/

    }

    private fun openFAQ() {
        coachingCoachBinding.apply {

            questionOneContainer.setOnClickListener {
                visibilityQA(
                    coachingCoachBinding.questionOne,
                    coachingCoachBinding.answerOneContainer
                )
            }

            question2Container.setOnClickListener {
                visibilityQA(coachingCoachBinding.question2, coachingCoachBinding.answer2Container)
            }

            question3Container.setOnClickListener {
                visibilityQA(coachingCoachBinding.question3, coachingCoachBinding.answer3Container)
            }

            question4Container.setOnClickListener {
                visibilityQA(coachingCoachBinding.question4, coachingCoachBinding.answer4Container)
            }

            question5Container.setOnClickListener {
                visibilityQA(coachingCoachBinding.question5, coachingCoachBinding.answer5Container)
            }

            question6Container.setOnClickListener {
                visibilityQA(coachingCoachBinding.question6, coachingCoachBinding.answer6Container)
            }

            question7Container.setOnClickListener {
                visibilityQA(coachingCoachBinding.question7, coachingCoachBinding.answer7Container)
            }

            question8Container.setOnClickListener {
                visibilityQA(coachingCoachBinding.question8, coachingCoachBinding.answer8Container)
            }

            question9Container.setOnClickListener {
                visibilityQA(coachingCoachBinding.question9, coachingCoachBinding.answer9Container)
            }

            question10Container.setOnClickListener {
                visibilityQA(
                    coachingCoachBinding.question10,
                    coachingCoachBinding.answer10Container
                )
            }

            question11Container.setOnClickListener {
                visibilityQA(
                    coachingCoachBinding.question11,
                    coachingCoachBinding.answer11Container
                )
            }

            question12Container.setOnClickListener {
                visibilityQA(
                    coachingCoachBinding.question12,
                    coachingCoachBinding.answer12Container
                )
            }

            question13Container.setOnClickListener {
                visibilityQA(
                    coachingCoachBinding.question13,
                    coachingCoachBinding.answer13Container
                )
            }

            question14Container.setOnClickListener {
                visibilityQA(
                    coachingCoachBinding.question14,
                    coachingCoachBinding.answer14Container
                )
            }

            question15Container.setOnClickListener {
                visibilityQA(
                    coachingCoachBinding.question15,
                    coachingCoachBinding.answer15Container
                )
            }

            question16Container.setOnClickListener {
                visibilityQA(
                    coachingCoachBinding.question16,
                    coachingCoachBinding.answer16Container
                )
            }

            question17Container.setOnClickListener {
                visibilityQA(
                    coachingCoachBinding.question17,
                    coachingCoachBinding.answer17Container
                )
            }

            question18Container.setOnClickListener {
                visibilityQA(
                    coachingCoachBinding.question18,
                    coachingCoachBinding.answer18Container
                )
            }

            question19Container.setOnClickListener {
                visibilityQA(
                    coachingCoachBinding.question19,
                    coachingCoachBinding.answer19Container
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