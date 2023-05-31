package com.fitness.fitnessCru.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.StudioFilterAdapter
import com.fitness.fitnessCru.databinding.FragmentStudioFilterBinding
import com.fitness.fitnessCru.model.StudioFilterModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class StudioFilterFragment : BottomSheetDialogFragment() {

    private lateinit var studioFilterBinding: FragmentStudioFilterBinding

    private lateinit var studioFilterAdapter: StudioFilterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        studioFilterBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_studio_filter, container, false)
        val filterChildItems: MutableList<StudioFilterModel.StudioFilterChildItem> = ArrayList()
        filterChildItems.apply {
            add(
                StudioFilterModel.StudioFilterChildItem(
                    "30 min"
                )
            )
            add(
                StudioFilterModel.StudioFilterChildItem(
                    "40 min"
                )
            )
            add(
                StudioFilterModel.StudioFilterChildItem(
                    "60 min"
                )
            )
        }

        val filterChildItems2: MutableList<StudioFilterModel.StudioFilterChildItem> = ArrayList()
        filterChildItems2.apply {
            add(
                StudioFilterModel.StudioFilterChildItem(
                    "Advance"
                )
            )
            add(
                StudioFilterModel.StudioFilterChildItem(
                    "Beginner"
                )
            )
            add(
                StudioFilterModel.StudioFilterChildItem(
                    "Intermediate"
                )
            )
        }


        val studioFilterModel: MutableList<StudioFilterModel> = ArrayList()
        studioFilterModel.apply {
            add(
                StudioFilterModel(
                    "Duration",
                    filterChildItems as ArrayList<StudioFilterModel.StudioFilterChildItem>
                )
            )
            add(
                StudioFilterModel(
                    "Intensity",
                    filterChildItems2 as ArrayList<StudioFilterModel.StudioFilterChildItem>
                )
            )
            setStudioFilterRV(studioFilterModel as ArrayList<StudioFilterModel>)
        }
        return studioFilterBinding.root
    }


    private fun setStudioFilterRV(studioFilterModel: ArrayList<StudioFilterModel>) {
        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        studioFilterBinding.filterRv.layoutManager = linearLayout
        studioFilterBinding.filterRv.setHasFixedSize(true)
        studioFilterAdapter = StudioFilterAdapter(studioFilterModel, context)
        studioFilterBinding.filterRv.adapter = studioFilterAdapter
    }
}