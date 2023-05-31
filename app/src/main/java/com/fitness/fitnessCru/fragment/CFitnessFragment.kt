package com.fitness.fitnessCru.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.databinding.FragmentCFitnessBinding

class CFitnessFragment : Fragment() {

    private lateinit var cFitnessBinding: FragmentCFitnessBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cFitnessBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_c_fitness, container, false)
        return cFitnessBinding.root


    }

}