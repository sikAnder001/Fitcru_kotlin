package com.fitness.fitnessCru.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.CoachSpecializationsAdapter
import com.fitness.fitnessCru.adapter.SelectCoachesAdapter
import com.fitness.fitnessCru.body.GetCoachListBody
import com.fitness.fitnessCru.body.GetCoachListBody2
import com.fitness.fitnessCru.databinding.FragmentPlanSelectTrainerBinding
import com.fitness.fitnessCru.interfaces.SelectCoachInterface
import com.fitness.fitnessCru.interfaces.SpecializationSelection
import com.fitness.fitnessCru.model.CoachBundle
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.GetCoachListResponse
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.CoachSpecializationsViewModel
import com.fitness.fitnessCru.vm_factory.CoachSpecializationsVMFactory

class PlanSelectTrainerFragment : Fragment() {

    private var selectTrainerbinding: FragmentPlanSelectTrainerBinding? = null

    private val binding get() = selectTrainerbinding!!

    private lateinit var loading: CustomProgressLoading

    private var coachCategoryId: Int = 0

    private var coachSlabType: Int = 0

    private lateinit var coachSpecializationsAdapter: CoachSpecializationsAdapter

    private lateinit var bundleData: CoachBundle

    private lateinit var repository: Repository

    private lateinit var viewModel: CoachSpecializationsViewModel

    private lateinit var factory: CoachSpecializationsVMFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bundleData = arguments?.getSerializable("data") as CoachBundle
        selectTrainerbinding = FragmentPlanSelectTrainerBinding.inflate(layoutInflater)
        binding.goBackBtn.setOnClickListener { activity?.onBackPressed() }
        loading = CustomProgressLoading(requireContext())
        repository = Repository()
        factory = CoachSpecializationsVMFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CoachSpecializationsViewModel::class.java]
        if (!bundleData.slabName.isNullOrEmpty()) {
            binding.tabName.text = "${bundleData.tabName} - ${bundleData.slabName}"
        } else binding.tabName.text = "${bundleData.tabName}"
        /*if (bundleData.slabName != "") {
            binding.slabName.apply {
                visibility = View.VISIBLE
                text = bundleData.slabName
            }
        } else binding.slabName.visibility = View.GONE*/
        coachCategoryId = bundleData.tabId
        coachSlabType = bundleData.slabId
        coachSpecializationsRV()
        getCoachSpecializationsList()
        selectCoaches()
        return selectTrainerbinding!!.root
    }

    private fun selectCoaches() {
        loading.showProgress()
        var selectCoachesAdapter = SelectCoachesAdapter(requireContext(), true)
        selectTrainerbinding!!.selectCoachesRv.adapter = selectCoachesAdapter
        viewModel.coachWithoutSpecialization(
            GetCoachListBody2(
                coachCategoryId, coachSlabType
            )
        )
        viewModel.coachList.observe(viewLifecycleOwner) {
            loading.hideProgress()
            if (it.isSuccessful && it.body() != null) {
                val body = it.body()!!
                if (body.data.isEmpty()) {
                    binding.toastTv.visibility = View.VISIBLE
                } else {
                    binding.toastTv.visibility = View.GONE
                }
                selectCoachesAdapter.setList(body.data)
            } else if (!it.isSuccessful && it.errorBody() != null)
                Util.error(it.errorBody(), GetCoachListResponse::class.java).message
            else "Something went wrong"
        }
        selectCoachesAdapter.setOnClickInterface(object : SelectCoachInterface {
            override fun onBookClicked(view: View, position: Int, nav: Int, bundleX: Bundle) {
                var bundle = Bundle()
                bundle.putInt("coachId", bundleX.getInt("coachId"))
                bundle.putString("key", bundleData.key)
                bundle.putString("price", bundleData.price)
                bundle.putInt("feeId", bundleData.feeId)
                bundle.putString("category", bundleData.tabName)
                bundle.putInt("categoryId", bundleData.tabId)
                bundle.putString("slab", bundleData.slabName)
                bundle.putInt("slabId", bundleData.slabId)
                bundle.putString("duration", bundleData.duration)
                bundle.putString("coach_name", bundleX.getString("coach_name"))
                val x = findNavController()
                if (nav == 0) {
                    x.navigate(
                        R.id.action_planSelectTrainerFragment_to_planDetailedPageFragment,
                        bundle
                    )
                } else if (nav == 1) {
                    x.navigate(
                        R.id.planSelectToTrainerProfile,
                        bundle
                    )
                }
            }
        })

    }

    private fun coachSpecializationsRV() {
        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.trainWithBestCoachesRV.layoutManager = linearLayout
        binding.trainWithBestCoachesRV.setHasFixedSize(true)
        coachSpecializationsAdapter = CoachSpecializationsAdapter(context)
        binding.trainWithBestCoachesRV.adapter = coachSpecializationsAdapter
        coachSpecializationsAdapter.setOnClick(object : SpecializationSelection {
            override fun onClick(id: Int) {
                loading.showProgress()
                viewModel.coachWithSpecialization(
                    GetCoachListBody(
                        coachCategoryId,
                        coachSlabType,
                        id
                    )
                )
            }
        })
    }

    private fun getCoachSpecializationsList() {
        viewModel.getCoachSpecializations()

        viewModel.coachSpecializations.observe(viewLifecycleOwner) {
            try {
                val token = Session.getToken()

                if (it.isSuccessful && it.body()?.error_code == 0) {
                    coachSpecializationsAdapter.setList(it.body()!!.data)

                } else Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show()
            } catch (e: Exception) {

                Util.toast(requireContext(), "Error : ${e.message}")

            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.coachWithoutSpecialization(
            GetCoachListBody2(
                coachCategoryId, coachSlabType
            )
        )
    }
}