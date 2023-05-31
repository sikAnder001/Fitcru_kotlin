package com.fitness.fitnessCru.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fitness.fitnessCru.adapter.*
import com.fitness.fitnessCru.databinding.FragmentWStudioBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.StudioViewModel
import com.fitness.fitnessCru.vm_factory.StudioVMFactory


class WStudioFragment : Fragment() {

    private lateinit var sBinding: FragmentWStudioBinding
    private val binding get() = sBinding!!

    private lateinit var repository: Repository
    private lateinit var viewModel: StudioViewModel
    private lateinit var factory: StudioVMFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sBinding = FragmentWStudioBinding.inflate(inflater, container, false)

        repository = Repository()

        factory = StudioVMFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(StudioViewModel::class.java)

        getStudio()

//        recycleTrainsWithCoach()

        return binding.root
    }

    private fun getStudio() {
        viewModel.getStudio()

        var userDetail = Session.getUserDetails()

        var allStudioAdapter = AllStudioAdapter(context)

        var trendingSessionsAdapter = TrendingSessionAdapterStudio(context, userDetail.ispaid)

        var newCollectionsAdapter = NewCollectionsAdapter(context)

        var workoutByAdapter = WorkoutByAdapter(context)

        var popularCollectionsAdapter = PopularCollectionsAdapter(context)

        var trainWithBestCoachAdapter = StudioTrainWithBestCoachAdapter(context)

//        var traniwithCoach = TraniwithCoach(context)

        binding.allStudioRV.adapter = allStudioAdapter

        binding.rvTrendingSessions.adapter = trendingSessionsAdapter

        binding.newCollectionsRV.adapter = newCollectionsAdapter

        binding.workoutByRV.adapter = workoutByAdapter

        binding.popularCollectionsRV.adapter = popularCollectionsAdapter

        binding.trainWithBestCoachesRV.adapter = trainWithBestCoachAdapter

        binding.apply {
            val array = arrayOf(
                allStudioRV,
                rvTrendingSessions,
                newCollectionsRV,
                workoutByRV,
                popularCollectionsRV,
                trainWithBestCoachesRV
            )
            for (i in array) Util.recyclerSlideIssueFix(i)
        }
        viewModel.response.observe(viewLifecycleOwner) {

            if (it.isSuccessful && it.code() == 200 && it.body() != null && it.body()!!.data != null) {

                it.body()!!.data.apply {

                    studios?.let { studio -> allStudioAdapter.setList(studio) }

                    trending_session?.let { to -> trendingSessionsAdapter.setList(to) }

                    workout_by_intensity?.let { flp -> workoutByAdapter.setList(flp) }

                    new_collection?.let { tPack -> newCollectionsAdapter.setList(tPack) }

                    popular_collection?.let { benefit -> popularCollectionsAdapter.setList(benefit) }

                    train_with_best_coach?.let { bTrainer ->
                        trainWithBestCoachAdapter.setList(
                            bTrainer
                        )
                    }

                }
            }
        }
    }

}