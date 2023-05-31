package com.fitness.fitnessCru.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.RazorPaymentActivity
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.adapter.PlanDetailedHowToMakeRVAdapter
import com.fitness.fitnessCru.databinding.FragmentPlanDetailedPageBinding
import com.fitness.fitnessCru.model.PlanDetailedHowToMakeRVModel
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.ChooseMonthlyPlanResponse
import com.fitness.fitnessCru.utility.Constants
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.CoachCategoryViewModel
import com.fitness.fitnessCru.vm_factory.CoachCategoryVMFactory
import com.google.android.material.chip.Chip
import kotlin.math.roundToInt


class PlanDetailedPageFragment : Fragment() {

    private lateinit var planDetailedBinding: FragmentPlanDetailedPageBinding
    private lateinit var howToMakeAdapter: PlanDetailedHowToMakeRVAdapter
    private lateinit var howToMakeModel: ArrayList<PlanDetailedHowToMakeRVModel>

    private lateinit var repository: Repository
    private lateinit var viewModel: CoachCategoryViewModel
    private lateinit var factory: CoachCategoryVMFactory

    private lateinit var loading: CustomProgressLoading

    private var coachId = 0
    private var feeId = 0
    private var duration = ""
    private var slab = ""
    private var category = ""
    private var feeStructureKey = ""
    private var price = ""
    private var categoryId = 0
    private var slabId = 0
    private var productName = ""

    private var amount = 0.0
    private var planid = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        planDetailedBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_plan_detailed_page,
            container,
            false
        )

        loading = CustomProgressLoading(requireContext())

        planDetailedBinding.backBtn.setOnClickListener { requireActivity().onBackPressed() }

        repository = Repository()

        factory = CoachCategoryVMFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(CoachCategoryViewModel::class.java)

        feeId = requireArguments().getInt("feeId", 0)
        feeStructureKey = requireArguments().getString("key", "")
        price = requireArguments().getString("price", "")
        categoryId = requireArguments().getInt("categoryId", 0)
        slabId = requireArguments().getInt("slabId", 0)
        coachId = requireArguments().getInt("coachId", 0)
        duration = requireArguments().getString("duration", "")
        slab = requireArguments().getString("slab", "")
        category = requireArguments().getString("category", "")
        productName = requireArguments().getString("coach_name", "")

        monthlyPlanCall(feeId, feeStructureKey, price, categoryId, slabId, coachId)

        Log.v("Tag", "${feeId} ${feeStructureKey} ${price} ${categoryId} ${slabId} ${coachId}")

        // TODO 8,3 only for testing reason , have to remove
        howToMakeRV()
        hereWhatUGetRV()

        planDetailedBinding.buyNowBtn.setOnClickListener {
            rezorpayCall();
        }
        return planDetailedBinding.root
    }

    private fun rezorpayCall() {
        val convertedAmount = Math.round(amount).toInt()
        val intent = Intent(requireActivity(), RazorPaymentActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("amount", convertedAmount)
        bundle.putInt("feeId", planid)
        bundle.putInt("planId", planid)
        bundle.putString("productName", productName)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun monthlyPlanCall(
        feeId: Int,
        feeStructureKey: String,
        price: String,
        categoryId: Int,
        slabId: Int,
        coachId: Int
    ) {
        loading.showProgress()
        viewModel.getChooseMonthlyPrice(
            feeId, feeStructureKey, price, categoryId, slabId, coachId
        )
        viewModel.monthlyPlanResponse.observe(viewLifecycleOwner) {
            loading.hideProgress()
            val body = it.body()!!
            if (it.isSuccessful && body != null && body.error_code == 0) {
                val resData = it.body()!!.data
                planid = resData.id
                getPlanDetail(coachId, planid /*8,3*/)

            } else if (it.isSuccessful && body != null && body!!.error_code == 1) {
                Util.toast(
                    requireContext(), it.body()!!.message
                )
            } else if (!it.isSuccessful && it.errorBody() != null) Util.toast(
                requireContext(),
                Util.error(it.errorBody(), ChooseMonthlyPlanResponse::class.java).message
            )
        }
    }

    private fun getPlanDetail(coachId: Int, feeId: Int) {
        try {
            viewModel.getPlanDetail(coachId, feeId)

            viewModel.getPlanDetailResponse.observe(viewLifecycleOwner) {
                //  loading.hideProgress()
                try {
                    if (it.isSuccessful && it.body() != null) {
                        val response = it.body()!!
                        planDetailedBinding.apply {
                            //duration is right now
                            detailsTv.text =
                                "$duration $category ${slab}"
                            durationTv.text = "$duration"
                            totalRsTv.text = response.data.total_price
                            priceDiscountRsTv.text =
                                "- ${response.data.discount_percent.roundToInt()}"
                            gstRsTv.text = response.data.gst.toString()
                            totalPayableRsTv.text = response.data.total_paybale.toString()
                            amount = response.data.total_paybale * 100

                            // setCoachPlanRV(response.data.coach_details)

                            coachNameTv.text = response.data.coach_details.coach_name
                            coachAddressTv.text =
                                response.data.coach_details?.coach_location?.location_name
                            chipGroup.removeAllViews()
                            response.data.coach_details.coach_specialization.forEach { its ->
                                chipGroup.addView(
                                    createTagChip(its.name.toString())
                                )
                            }
                            /* tvAvailableCount.text = response.data.coach_details.no_of_slots*/

                            coachProfileTv.setOnClickListener {
                                val intent = Intent(
                                    context?.applicationContext,
                                    SetupAllActivity::class.java
                                )
                                intent.putExtra(Constants.DESTINATION, Constants.COACH_PROFILE)
                                intent.putExtra("coachId", response.data.coach_details.id)
                                context?.startActivity(intent)
                            }

                            if (context != null) {
                                Glide.with(requireContext())
                                    .load(response.data.coach_details.image_url)
                                    .placeholder(
                                        requireContext().resources.getDrawable(
                                            R.drawable.place_holder,
                                            null
                                        )
                                    )
                                    .into(planDetailedThumbnailImage)
                                Glide.with(requireContext())
                                    .load(response.data.coach_details.image_url)
                                    .placeholder(
                                        requireContext().resources.getDrawable(
                                            R.drawable.place_holder,
                                            null
                                        )
                                    )
                                    .into(coachImg)
                            }
                            //response.data.coach_details.coach_specialization.forEach { its -> chipGroup.addView(createTagChip(its.name)) }
                        }

                    } else Toast.makeText(
                        requireContext(),
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } catch (e: Exception) {
                    Util.toast(requireContext(), "Error : ${e.message}")
                }
            }
        } catch (e: Exception) {
            Util.toast(requireContext(), "Error : ${e.message}")
        }
    }

    private fun howToMakeRV() {
        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        planDetailedBinding.howToMakeRv.layoutManager = linearLayout
        planDetailedBinding.howToMakeRv.setHasFixedSize(true)
        howToMakeModel = setDataInHowToMake()
        howToMakeAdapter = PlanDetailedHowToMakeRVAdapter(howToMakeModel, context)
        planDetailedBinding.howToMakeRv.adapter = howToMakeAdapter
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

    private fun hereWhatUGetRV() {
        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        planDetailedBinding.hereWhatGetRv.layoutManager = linearLayout
        planDetailedBinding.hereWhatGetRv.setHasFixedSize(true)
        howToMakeModel = setDataInHereWhatUGet()
        howToMakeAdapter = PlanDetailedHowToMakeRVAdapter(howToMakeModel, context)
        planDetailedBinding.hereWhatGetRv.adapter = howToMakeAdapter
    }

    private fun setDataInHereWhatUGet(): ArrayList<PlanDetailedHowToMakeRVModel> {
        val what: ArrayList<PlanDetailedHowToMakeRVModel> = ArrayList()
        what.apply {
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
        return what
    }


    //    private fun setCoachPlanRV(coachDetails: GetPlanDetailResponse.Data.CoachDetails) {
//        val linearLayout = LinearLayoutManager(
//            context, LinearLayoutManager.HORIZONTAL, false
//        )
//        planDetailedBinding.coachSelectedWithThePlanRv.layoutManager = linearLayout
//        planDetailedBinding.coachSelectedWithThePlanRv.setHasFixedSize(true)
//        planDetailedPageAdapter = SelectCoachesAdapter(requireContext(), false)
//        planDetailedPageAdapter.setOnClickInterface(object : SelectCoachInterface {
//            override fun onBookClicked(view: View, position: Int, nav: Int, bundle: Bundle) {
//                TODO("Not yet implemented")
//            }
//        })
//        planDetailedBinding.coachSelectedWithThePlanRv.adapter = planDetailedPageAdapter
//     //   planDetailedPageAdapter.setList(coachDetails)
//    }
    private fun createTagChip(chipName: String): Chip {
        return Chip(context).apply {
            text = "$chipName"
            setChipBackgroundColorResource(R.color.overlay_dark_50)
            setTextColor(ContextCompat.getColor(context, R.color.white))
            setTextAppearance(R.style.ChipTextAppearance)
        }
    }

}