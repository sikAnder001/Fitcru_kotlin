package com.fitness.fitnessCru.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.WorkoutByFitnessAdapter
import com.fitness.fitnessCru.databinding.FragmentCardioBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.StudioDetailsViewModel
import com.fitness.fitnessCru.vm_factory.StudioDetailsVMFactory

class WorkoutByFitnessFragment : Fragment() {

    private lateinit var binding: FragmentCardioBinding

    private lateinit var loading: CustomProgressLoading

    private lateinit var adapter: WorkoutByFitnessAdapter

    private lateinit var repository: Repository

    private lateinit var viewModel: StudioDetailsViewModel

    private lateinit var factory: StudioDetailsVMFactory

    private var levelId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cardio, container, false)

        loading = CustomProgressLoading(requireContext())

        binding.apply {
            tvTitle.text = arguments?.getString("title")

            levelId = arguments?.getInt("id")!!
        }

        repository = Repository()

        factory = StudioDetailsVMFactory(repository)

        viewModel = ViewModelProvider(this, factory)[StudioDetailsViewModel::class.java]

        cardioDataSet()

        getCardioList()

        binding.backBtn.setOnClickListener { activity?.onBackPressed() }

        return binding.root
    }

    private fun cardioDataSet() {
        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.cardioRv.layoutManager = linearLayout

        binding.cardioRv.setHasFixedSize(true)

        adapter = WorkoutByFitnessAdapter(context)

        binding.cardioRv.adapter = adapter
    }

    private fun getCardioList() {
        loading.showProgress()

        viewModel.getWorkoutByFitness(levelId)

        viewModel.getWorkoutByFitness.observe(viewLifecycleOwner) {

            loading.hideProgress()

            val token = Session.getToken()
            if (it.isSuccessful && it.code() == 200 && it.body() != null) {
                val body = it.body()!!
                try {
                    if (body.data != null) {
                        adapter.setList(body.data!!)
                        binding.tvSessionCount.text = "${body.data.size} Sessions"
                    } else {
                        binding.toastTv.visibility = View.VISIBLE
                    }

                } catch (e: Exception) {
                    Util.toast(requireContext(), "Error : ${e.message}")
                }

            } else {
                binding.toastTv.visibility = View.VISIBLE
            }

        }
    }
}