package com.fitness.fitnessCru.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.LoginMainActivity
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.adapter.ReviewsAdapter
import com.fitness.fitnessCru.adapter.TCoachCertificateAdapter
import com.fitness.fitnessCru.adapter.TCoachSpecializationAdapter
import com.fitness.fitnessCru.databinding.FragmentTrainerProfileBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.GetCoachDetailsResponse
import com.fitness.fitnessCru.utility.Constants
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.CoachCategoryViewModel
import com.fitness.fitnessCru.vm_factory.CoachCategoryVMFactory

class TrainerProfileFragment : Fragment() {

    private lateinit var binding: FragmentTrainerProfileBinding

    private lateinit var coachCertificateAdapter: TCoachCertificateAdapter

    private lateinit var coachReviewsAdapter: ReviewsAdapter

    private lateinit var coachSpecializationAdapter: TCoachSpecializationAdapter

    private lateinit var repository: Repository

    private lateinit var viewModel: CoachCategoryViewModel

    private lateinit var factory: CoachCategoryVMFactory

    private lateinit var loading: CustomProgressLoading

    var newDataCertificate = ArrayList<GetCoachDetailsResponse.CoachCertificate>()

    private var coachId: Int = 0
    private var planId = 0
    private var catId = 0
    private var coachName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrainerProfileBinding.inflate(layoutInflater)

        loading = CustomProgressLoading(requireContext())

        binding.goBackBtn.setOnClickListener { requireActivity().onBackPressed() }

        repository = Repository()

        factory = CoachCategoryVMFactory(repository)

        viewModel = ViewModelProvider(this, factory)[CoachCategoryViewModel::class.java]

        coachName = requireArguments()?.getString("coachName", "").toString()

        if (!coachName.isNullOrEmpty()) {
            planId = requireArguments().getInt("planId", 0)
            catId = requireArguments().getInt("catId", 0)
            coachId = requireArguments().getInt("coachId", 0)
            coachName = requireArguments().getString("coachName").toString()
            binding.filterBtn.visibility = View.VISIBLE
            binding.filterBtn.setOnClickListener {
                val intent = Intent(context, SetupAllActivity::class.java)
                intent.putExtra(Constants.DESTINATION, Constants.COACH_PLAN_DETAIL)
                intent.putExtra("planId", planId)
                intent.putExtra("catId", catId)
                intent.putExtra("coachId", coachId)
                intent.putExtra("coachName", coachName)
                startActivity(intent)
            }
        } else {
            coachId = requireArguments().getInt("coachId")
            binding.filterBtn.visibility = View.GONE
        }

        getCoachDetails()

        coachCertificateRV()

        coachReviewRV()

        coachSpecializationRV()

        return binding.root
    }

    private fun getCoachDetails() {
        viewModel.getCoachDetails(coachId)
        loading.showProgress()
        viewModel.getCoachResponse.observe(viewLifecycleOwner) {
            loading.hideProgress()
            if (it.isSuccessful && it.code() == 200 && it.body() != null) {

                try {

                    val token = Session.getToken()

                    var data = it.body()!!.data

                    var address = data.coach_location?.location_name

                    binding.apply {

                        Glide.with(requireContext()).load(data.image_url)
                            .placeholder(R.drawable.place_holder)
                            .into(binding.coachImg)

                        coachNameTv.text = data.coach_name

                        coachBioData.text = data.coach_biodata

                        coachQualifications.text = data.coach_qualifications

                        /*slotsPresentTv.text = data.available_slots*/

                        coachLocation.text = "(${address})"
                        newDataCertificate = ArrayList()
                        if (!data.coach_certificate.isNullOrEmpty()) {
                            for (element in data.coach_certificate) {
                                if (element.status == "Approved") {
                                    newDataCertificate.add(element)
                                } else {
                                    continue
                                }
                            }
                            coachCertificateAdapter.setList(newDataCertificate)
                        } else toastTv2.visibility = View.VISIBLE

                        /*coachReviewsAdapter.setList(data.coach_reviews)*/

                        if (!data.coach_specialization.isNullOrEmpty()) {
                            coachSpecializationAdapter.setList(data.coach_specialization)
                        } else toastTv1.visibility = View.VISIBLE
                    }

                } catch (e: Exception) {
                    Util.toast(requireContext(), "Error : ${e.message}")
                }

            } else if (it.code() == 404) {
            } else if (it.code() == 401) {
                startActivity(Intent(requireContext(), LoginMainActivity::class.java))
                requireActivity().finishAffinity()
            }
        }
    }

    private fun coachCertificateRV() {
        binding.coachCertificateRv.setHasFixedSize(true)
        coachCertificateAdapter = TCoachCertificateAdapter(context)
        binding.coachCertificateRv.adapter = coachCertificateAdapter
    }

    private fun coachReviewRV() {
        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.reviewRv.layoutManager = linearLayout
        binding.reviewRv.setHasFixedSize(true)
        coachReviewsAdapter = ReviewsAdapter(context)
        binding.reviewRv.adapter = coachReviewsAdapter
    }

    private fun coachSpecializationRV() {
        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.coachSpecializationsRv.layoutManager = linearLayout
        binding.coachSpecializationsRv.setHasFixedSize(true)
        coachSpecializationAdapter = TCoachSpecializationAdapter(context)
        binding.coachSpecializationsRv.adapter = coachSpecializationAdapter
    }

}