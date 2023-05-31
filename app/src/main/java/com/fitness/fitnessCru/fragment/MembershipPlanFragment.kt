package com.fitness.fitnessCru.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.adapter.MembershipPlanAdapter
import com.fitness.fitnessCru.adapter.MembershipPlanCoachAdapter
import com.fitness.fitnessCru.databinding.FragmentMembershipPlanBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.MembershipPlanViewModel
import com.fitness.fitnessCru.vm_factory.MembershipPlanVMFactory

class MembershipPlanFragment : Fragment() {

    private lateinit var membershipBinding: FragmentMembershipPlanBinding

    private lateinit var loading: CustomProgressLoading

    private lateinit var repository: Repository

    private lateinit var viewModel: MembershipPlanViewModel

    private lateinit var factory: MembershipPlanVMFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        membershipBinding = FragmentMembershipPlanBinding.inflate(inflater, container, false)

        loading = CustomProgressLoading(requireContext())

        repository = Repository()

        factory = MembershipPlanVMFactory(repository)

        viewModel = ViewModelProvider(this, factory)[MembershipPlanViewModel::class.java]

        getPlan()

        return membershipBinding.root
    }

    private fun getPlan() {

        loading.showProgress()

        viewModel.getMembershipPlan()

        var planAdapter = MembershipPlanAdapter(context)

        var coachAdapter = MembershipPlanCoachAdapter(context)

        membershipBinding.apply {

            membershipPlanRv.adapter = planAdapter

            coachListRv.adapter = coachAdapter
        }

        viewModel.response.observe(viewLifecycleOwner) {

            if (it.isSuccessful && it.code() == 200 && it.body() != null) {

                loading.hideProgress()

                try {

                    val token = Session.getToken()

                    if (it.body()!!.Plans.isNullOrEmpty()) {
//                        membershipBinding.toastTv.visibility = View.VISIBLE

                    } else {
                        membershipBinding.programTv.visibility = View.VISIBLE
                        planAdapter.setList(it.body()!!.Plans!!)

                    }
                    if (it.body()!!.Coaches.isEmpty()) {
                        membershipBinding.toastTv1.visibility = View.VISIBLE
                    } else {
                        membershipBinding.toastTv1.visibility = View.GONE
                        coachAdapter.setList(it.body()!!.Coaches)

                    }

                } catch (e: Exception) {
                    Util.toast(requireContext(), "Error : ${e.message}")
                }

            }
        }
    }
}