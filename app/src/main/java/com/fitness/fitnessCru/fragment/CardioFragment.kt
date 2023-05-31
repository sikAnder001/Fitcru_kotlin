package com.fitness.fitnessCru.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.CardioAdapter
import com.fitness.fitnessCru.databinding.FragmentCardioBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.StudioDetailsViewModel
import com.fitness.fitnessCru.vm_factory.StudioDetailsVMFactory

class CardioFragment : Fragment() {

    private lateinit var cardioBinding: FragmentCardioBinding

    private var TAG = CardioFragment::class.java.simpleName

    private lateinit var loading: CustomProgressLoading

    private lateinit var cardioAdapter: CardioAdapter

    private lateinit var repository: Repository

    private lateinit var viewModel: StudioDetailsViewModel

    private lateinit var factory: StudioDetailsVMFactory

    private var studioId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cardioBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cardio, container, false)

        loading = CustomProgressLoading(requireContext())

        cardioBinding.apply {
            tvTitle.text = arguments?.getString("title")

            studioId = arguments?.getInt("id")!!
        }

        repository = Repository()

        factory = StudioDetailsVMFactory(repository)

        viewModel = ViewModelProvider(this, factory)[StudioDetailsViewModel::class.java]

        cardioDataSet()

        getCardioList()

        cardioBinding.backBtn.setOnClickListener { activity?.onBackPressed() }

        return cardioBinding.root
    }

    private fun cardioDataSet() {
        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        cardioBinding.cardioRv.layoutManager = linearLayout

        cardioBinding.cardioRv.setHasFixedSize(true)

        cardioAdapter = CardioAdapter(context, object : CardioAdapter.AvailabilityInterface {
            override fun onClick(workout_id: Int, program_id: Int) {
                val bundle = Bundle()
                bundle.putInt("workout_id", workout_id)
                bundle.putInt("program_id", program_id)
                Navigation.findNavController(
                    context as Activity,
                    R.id.nutrition_setup_fragment_container
                ).navigate(R.id.sncSessionFragment, bundle)
            }

        })

        cardioBinding.cardioRv.adapter = cardioAdapter
    }

    private fun getCardioList() {
        loading.showProgress()

        viewModel.getStudioDetails(studioId)

        viewModel.response.observe(viewLifecycleOwner) {

            loading.hideProgress()

            val token = Session.getToken()
            if (it.isSuccessful && it.code() == 200 && it.body() != null) {
                val body = it.body()!!
                try {
                    if (body.data?.workouts != null) {
                        cardioAdapter.setCardioList(body.data?.workouts!!)
                        cardioBinding.tvSessionCount.text = "${body.data.workouts.size} Sessions"
                    } else {
                        cardioBinding.toastTv.visibility = View.VISIBLE
                    }

                } catch (e: Exception) {
                    Util.toast(requireContext(), "Error : ${e.message}")
                }

            } else {
                cardioBinding.toastTv.visibility = View.VISIBLE
            }

        }
    }
}