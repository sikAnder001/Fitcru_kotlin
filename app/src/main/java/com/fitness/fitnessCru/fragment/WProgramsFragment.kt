package com.fitness.fitnessCru.fragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.*
import com.fitness.fitnessCru.databinding.FragmentWProgramsBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.viewmodel.WorkoutProgramViewModel
import com.fitness.fitnessCru.vm_factory.WorkoutProgramVMFactory
import java.util.*

class WProgramsFragment : Fragment() {

    lateinit var programBinding: FragmentWProgramsBinding
    private lateinit var myAdapter: WSliderPagerAdapter
    private lateinit var timer: Timer
    private lateinit var handler: Handler
    private lateinit var repository: Repository
    private lateinit var viewModel: WorkoutProgramViewModel
    private lateinit var factory: WorkoutProgramVMFactory
    private lateinit var loading: CustomProgressLoading

    private lateinit var happyCustomerAdapter: WorkoutHappyCustomerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        programBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_w_programs, container, true)

        loading = CustomProgressLoading(requireContext())

        repository = Repository()

        factory = WorkoutProgramVMFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(WorkoutProgramViewModel::class.java)

        getProgram()
        sliderRun()
        setHappyCustomer()

        return programBinding.root
    }

    private fun setHappyCustomer() {
        happyCustomerAdapter = WorkoutHappyCustomerAdapter(requireContext())
        programBinding.happyCustomerVp.adapter = happyCustomerAdapter
        programBinding.dotsIndicator.setViewPager2(programBinding.happyCustomerVp)
    }

    private fun sliderRun() {

        myAdapter = WSliderPagerAdapter(requireContext())
        programBinding.viewpager.adapter = myAdapter

        myAdapter.list.observe(viewLifecycleOwner) {
            it!!.apply {
                programBinding.indicator.setViewPager(programBinding.viewpager)
                handler = Handler()
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        handler.post {
                            var i = programBinding.viewpager.currentItem
                            if (i == it.size - 1) {
                                i = 0
                                programBinding.viewpager.setCurrentItem(i, true)
                            } else {
                                i++
                                programBinding.viewpager.setCurrentItem(i, true)
                            }
                        }
                    }
                }, 4000, 4000)
            }
        }
    }

    private fun getProgram() {

        var userDetail = Session.getUserDetails()

        loading.showProgress()

        viewModel.getProgram()

        val workOutGoalAdapter = WorkOutGoalParentAdapter(context, userDetail.ispaid)

        val workOutGoalAdapter2 = WorkOutGoalParentAdapter(context, userDetail.ispaid)

        val workOutTrendingOffersAdapter = WorkOutTrendingOffersAdapter(context, userDetail.ispaid)

        val workOutBenefitsAdapter = WorkOutBenefitsAdapter(context)

        programBinding.rvGoals.adapter = workOutGoalAdapter

        programBinding.rvGoals2.adapter = workOutGoalAdapter2

        programBinding.rvTrendingOffers.adapter = workOutTrendingOffersAdapter

        programBinding.rvBenefits.adapter = workOutBenefitsAdapter

        viewModel.response.observe(viewLifecycleOwner) {

            loading.hideProgress()

            if (it.isSuccessful && it.code() == 200 && it.body() != null) {

                it.body()!!.data.apply {

                    if (trending_offers.size > 0)
                        programBinding.tvPlaceHolder.visibility = View.INVISIBLE
                    else {
                        programBinding.tvPlaceHolder.visibility = View.VISIBLE
                        programBinding.tvPlaceHolder.text = "Trending Offers Not Available"
                    }

                    trending_offers.let { to -> workOutTrendingOffersAdapter.setList(to) }
                    benifits.let { to -> workOutBenefitsAdapter.setList(to) }

                    goals_with_program.let { to ->
                        try {
                            if (to.size > 0 && to.size == 1)
                                workOutGoalAdapter.setList(ArrayList(to.subList(0, 1)))
                            else if (to.size > 1)
                                workOutGoalAdapter.setList(ArrayList(to.subList(0, 2)))
                            workOutGoalAdapter2.setList(ArrayList(to.subList(2, to.size)))
                        } catch (e: Exception) {
                            Log.d("error", e.message.toString())
                        }
                    }


                    programs_sliders.let { slider -> myAdapter.setList(slider) }

                    //happy_customers.let { happy -> happyCustomerAdapter.setList(happy) }
                }
            }
        }
    }
}