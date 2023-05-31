package com.fitness.fitnessCru.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.ViewProfileActivity
import com.fitness.fitnessCru.adapter.CoachingPageAdapter
import com.fitness.fitnessCru.databinding.FragmentCoachingBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.CoachCategoryResponse
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.CoachCategoryViewModel
import com.fitness.fitnessCru.vm_factory.CoachCategoryVMFactory
import com.google.android.material.tabs.TabLayoutMediator

class Coaching : Fragment() {

    private lateinit var coachingBinding: FragmentCoachingBinding

    private lateinit var repository: Repository
    private lateinit var viewModel: CoachCategoryViewModel
    private lateinit var factory: CoachCategoryVMFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        coachingBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_coaching, container, false)

        coachingBinding.apply {
            var userDetail = Session.getUserDetails()

            if (userDetail.name == null) {
                if (userDetail.email != null) {
                    var n = userDetail.email[0].toString().uppercase()
                    gobackbtn.text = n
                } else {
                    gobackbtn.visibility = View.GONE
                    placeholder.visibility = View.VISIBLE
                }
            } else {
                var n = userDetail.name[0].toString().uppercase()
                gobackbtn.text = n
            }

            placeholder.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext()!!,
                        ViewProfileActivity::class.java
                    )
                )
            }

            gobackbtn.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext()!!,
                        ViewProfileActivity::class.java
                    )
                )
            }
        }
        repository = Repository()

        factory = CoachCategoryVMFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(CoachCategoryViewModel::class.java)

        getTabCategory()
        return coachingBinding.root
    }

    private fun getTabCategory() {
        //loading.showProgress()
        viewModel.getCoachCategory()
        viewModel.response.observe(viewLifecycleOwner) {
            //  loading.hideProgress()
            try {
                if (it.isSuccessful && it.body() != null) {
                    val response = it.body()

                    fillTab(response)

                } else Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show()
            } catch (e: Exception) {
                Util.toast(requireContext(), "Error : ${e.message}")
            }
        }
    }

    private fun fillTab(response: CoachCategoryResponse?) {

        coachingBinding.apply {

            coachingViewPager.adapter = CoachingPageAdapter(this@Coaching, response)

            TabLayoutMediator(coachingTabLayout, coachingViewPager, true, false) { tab, position ->

                tab.text = response!!.data[position].name

            }.attach()
        }
    }
}