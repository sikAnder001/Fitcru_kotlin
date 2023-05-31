package com.fitness.fitnessCru.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.VideoPlayerActivity
import com.fitness.fitnessCru.adapter.SNCAdapter
import com.fitness.fitnessCru.adapter.SNCSessionWeekAdapter
import com.fitness.fitnessCru.databinding.FragmentSNCProgramBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.viewmodel.SNCProgramViewModel
import com.fitness.fitnessCru.vm_factory.SNCProgramVMFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SNCProgramFragment : Fragment() {

    private lateinit var sncProgramBinding: FragmentSNCProgramBinding

    private lateinit var sncWeekAdapter: SNCSessionWeekAdapter

    private lateinit var repository: Repository
    private lateinit var viewModel: SNCProgramViewModel
    private lateinit var factory: SNCProgramVMFactory

    private lateinit var loading: CustomProgressLoading
    private var program_id = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sncProgramBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_s_n_c_program, container, false)
        sncProgramBinding.backBtn.setOnClickListener { activity?.onBackPressed() }

        loading = CustomProgressLoading(requireContext())

        /*sncProgramBinding.notificationIcon.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("workout_id", bundle.getInt("workout_id"))
            Log.e("TAG", "onCreateView: ${bundle.toString()}")
            Navigation.findNavController(
                context as Activity,
                R.id.nutrition_setup_fragment_container
            ).navigate(R.id.sncSessionFragment, bundle)
        }*/

        repository = Repository()

        factory = SNCProgramVMFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(SNCProgramViewModel::class.java)

        getProgramDetail()

        return sncProgramBinding.root
    }

    private fun getProgramDetail() {
        try {
            sncProgramBinding.sessionRv.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            (sncProgramBinding.sessionRv.layoutManager as LinearLayoutManager).isAutoMeasureEnabled =
                false

            program_id = arguments?.getInt("id") ?: 0
            val args2 = arguments?.getInt("program_id") ?: 0

            loading.showProgress()
            viewModel.getDetail(program_id.toString())
            viewModel.response.observe(viewLifecycleOwner) {
                if (it.isSuccessful && it.code() == 200 && it.body() != null) {
                    sncProgramBinding.apply {
                        try {
                            var data = it.body()!!.data
                            sessionTv.text = data.description
                            sncProgramBinding.sncProgramText.text = data.name
                            sncProgramBinding.fitnessLevel.text = data.fitness_level.name

                            sessionCount.text = "${data.totalsession} Sessions"
                            Glide.with(requireContext()).load(data.image_url)
                                .placeholder(R.drawable.place_holder)
                                .into(sncProgramBinding.image)
                            sncProgramBinding.durationTime.text =
                                ("${data.duration_weeks} Weeks")
                            lifecycleScope.launch {
                                withContext(Dispatchers.Main) {
                                    sncWeekAdapter = SNCSessionWeekAdapter(
                                        data.sessionlist,
                                        args2,
                                        context,
                                        object : SNCAdapter.SelectVideoListener {
                                            override fun onClick(video: String) {
                                                Log.v("video Link", video)
                                                val intent = Intent(
                                                    requireActivity(),
                                                    VideoPlayerActivity::class.java
                                                )
                                                val bundle = Bundle()
                                                bundle.putString("url", video)
                                                intent.putExtras(bundle)
                                                startActivity(intent)
                                            }
                                        }) { loading.hideProgress() }
                                    sncProgramBinding.sessionRv.adapter = sncWeekAdapter
                                }
                            }
                        } catch (e: Exception) {
                            Log.v("Tag", e.message.toString())
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.v("Tag", e.message.toString())
        }
    }
}