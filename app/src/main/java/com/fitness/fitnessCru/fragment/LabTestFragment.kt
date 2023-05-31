package com.fitness.fitnessCru.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fitness.fitnessCru.databinding.FragmentLabTestBinding

class LabTestFragment : Fragment() {

    private lateinit var labTestBinding: FragmentLabTestBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        labTestBinding =
            FragmentLabTestBinding.inflate(inflater, container, false)

        hittingViews()

        return labTestBinding.root
    }

    private fun hittingViews() {
        labTestBinding.apply {
            backBtn.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }
}