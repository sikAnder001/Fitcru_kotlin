package com.fitness.fitnessCru.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.activities.ViewProfileActivity
import com.fitness.fitnessCru.adapter.CoachListAdapter
import com.fitness.fitnessCru.body.CoachData
import com.fitness.fitnessCru.databinding.FragmentCoachListBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.AvailableTimeSlotResponse
import com.fitness.fitnessCru.utility.Constants
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.AppointmentViewModel
import com.fitness.fitnessCru.vm_factory.AppointmentVMFactory

class CoachListFragment : Fragment() {

    private lateinit var binding: FragmentCoachListBinding

    private lateinit var viewModel: AppointmentViewModel

    private lateinit var loading: CustomProgressLoading

    lateinit var coachListAdapter: CoachListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCoachListBinding.inflate(inflater, container, false)

        loading = CustomProgressLoading(requireContext())

        var userDetail = Session.getUserDetails()

        if (userDetail.name == null) {
            if (userDetail.email != null) {
                var n = userDetail.email[0].toString().uppercase()
                binding.gobackbtn.text = n
            } else {
                binding.gobackbtn.visibility = View.GONE
                binding.placeholder.visibility = View.VISIBLE
            }
        } else {
            var n = userDetail.name[0].toString().uppercase()
            binding.gobackbtn.text = n
        }

        binding.placeholder.setOnClickListener {
            startActivity(
                Intent(
                    requireContext()!!,
                    ViewProfileActivity::class.java
                )
            )
        }
        binding.gobackbtn.setOnClickListener {
            startActivity(
                Intent(
                    requireContext()!!,
                    ViewProfileActivity::class.java
                )
            )
        }


        val repository by lazy { Repository() }
        val vmFactory by lazy { AppointmentVMFactory(repository) }
        viewModel = ViewModelProvider(this, vmFactory)[AppointmentViewModel::class.java]

        getCoachList()
        return binding.root
    }

    private fun getCoachList() {
        coachListAdapter =
            CoachListAdapter(context, object : CoachListAdapter.CoachListInterface {
                override fun onClick(
                    coachId: Int,
                    coach_name: String?,
                    image_url: String?,
                    location: String?,
                    years_experience: Int?,
                    clients_experience: Int?,
                    rating: Int?
                ) {
                    val intent = Intent(context?.applicationContext, SetupAllActivity::class.java)
                    intent.putExtra(Constants.DESTINATION, Constants.SCHEDULE_APPOINTMENT)
//                intent.putExtra("id", coachId)
                    intent.putExtra(
                        "data",
                        CoachData(
                            coachId,
                            coach_name,
                            image_url,
                            location,
                            years_experience,
                            clients_experience,
                            rating
                        )
                    )
                    context?.startActivity(intent)
                }
            })

        binding.coachListRv.adapter = coachListAdapter

        loading.showProgress()
        viewModel.getCoachList()
        viewModel.coachList.observe(viewLifecycleOwner) {
            loading.hideProgress()
            if (it.isSuccessful && it.body() != null) {
                if (it.body()!!.data != null) {
                    coachListAdapter.setList(it.body()!!.data)
                } else {
                    binding.toastTv.visibility = View.VISIBLE
                }
            } else if (!it.isSuccessful && it.errorBody() != null) {
                Util.error(it.errorBody(), AvailableTimeSlotResponse::class.java).message
            } else "Something went wrong"
        }

    }

}