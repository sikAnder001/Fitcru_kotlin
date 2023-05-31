package com.fitness.fitnessCru.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.activities.DashboardActivity
import com.fitness.fitnessCru.adapter.SelectCoachAdapter
import com.fitness.fitnessCru.databinding.FragmentSelectCoachTypeBinding
import com.fitness.fitnessCru.model.CoachSlabResponse
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.CoachTypeResponse
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.CoachCategoryViewModel
import com.fitness.fitnessCru.vm_factory.CoachCategoryVMFactory


class SelectCoachTypeFragment : Fragment() {

    private lateinit var selectBinding: FragmentSelectCoachTypeBinding

    private lateinit var repository: Repository

    private lateinit var viewModel: CoachCategoryViewModel

    private lateinit var factory: CoachCategoryVMFactory

    private lateinit var loading: CustomProgressLoading

    private var planId = 0
    private var num = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        selectBinding = FragmentSelectCoachTypeBinding.inflate(inflater, container, false)
        selectBinding.apply {
            gobackbtn.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }

        selectBinding.skipTv.setOnClickListener {
            val gotoDashBoard = Intent(requireActivity(), DashboardActivity::class.java)
            startActivity(gotoDashBoard)
        }


        loading = CustomProgressLoading(requireContext())

        repository = Repository()

        factory = CoachCategoryVMFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(CoachCategoryViewModel::class.java)


        planId = arguments?.getInt("planId")!!.toInt()
        num = arguments?.getString("num").toString()

        Log.v("num", num.toString())
        getAllCoach()

        return selectBinding.root
    }

    private fun getAllCoach() {
        val selectCoachAdapter = SelectCoachAdapter(context, planId, num)

        selectBinding.selectCoachTypeRV.adapter = selectCoachAdapter
        loading.showProgress()

        viewModel.getCoachByType()
        viewModel.coachType.observe(viewLifecycleOwner) {
            loading.hideProgress()
            if (it.isSuccessful && it.body() != null && it.body()!!.error_code == 0) {
                val data = it.body() as CoachTypeResponse
                selectCoachAdapter.setList(data.data)
                //coachSlabAdapter.setList(it.body()!!.data)

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
    }

}