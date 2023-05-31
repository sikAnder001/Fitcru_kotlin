package com.fitness.fitnessCru.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.VideoPlayerActivity
import com.fitness.fitnessCru.adapter.HabitAdapter
import com.fitness.fitnessCru.databinding.FragmentWHabitsBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.Session
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.HabitsViewModel
import com.fitness.fitnessCru.vm_factory.HabitsVMFactory

class WHabitsFragment : Fragment() {

    private lateinit var wHabitsBinding: FragmentWHabitsBinding

    private lateinit var habitAdapter: HabitAdapter

    private lateinit var repository: Repository

    private lateinit var viewModel: HabitsViewModel

    private lateinit var factory: HabitsVMFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        wHabitsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_w_habits, container, false)

        repository = Repository()

        factory = HabitsVMFactory(repository)

        viewModel = ViewModelProvider(this, factory)[HabitsViewModel::class.java]

        gatHabitData()

        setDataInHabitList()

        return wHabitsBinding.root
    }

    private fun gatHabitData() {

        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        wHabitsBinding.habitRv.layoutManager = linearLayout

        wHabitsBinding.habitRv.setHasFixedSize(true)

        habitAdapter = HabitAdapter(requireContext(), object : HabitAdapter.SelectVideoListener {
            override fun onClick(video: String) {
                Log.v("vido Link", video)
                val intent = Intent(
                    requireActivity(),
                    VideoPlayerActivity::class.java
                )
                val bundle = Bundle()
                bundle.putString("url", video)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })

        wHabitsBinding.habitRv.adapter = habitAdapter

    }

    private fun setDataInHabitList() {
        viewModel.getHabitsList()

        viewModel.response.observe(viewLifecycleOwner) {
            try {
                val token = Session.getToken()

                if (it.isSuccessful && it.body()?.error_code == 0) {

                    if (!it.body()!!.data.isNullOrEmpty()) {
                        habitAdapter.setList(it.body()!!.data)
                    } else wHabitsBinding.toastTv.visibility = View.VISIBLE

                } else Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show()


            } catch (e: Exception) {

                Util.toast(requireContext(), "Error : ${e.message}")

            }
        }
    }

}