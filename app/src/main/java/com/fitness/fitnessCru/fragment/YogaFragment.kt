package com.fitness.fitnessCru.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.YogaAdapter
import com.fitness.fitnessCru.databinding.FragmentYogaBinding
import com.fitness.fitnessCru.model.YogaModel


class YogaFragment : Fragment() {

    private lateinit var yogaBinding: FragmentYogaBinding

    private lateinit var yogaAdapter: YogaAdapter
    private lateinit var yogaModel: ArrayList<YogaModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        yogaBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_yoga, container, false)
        yogaDataSet()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
        yogaBinding.backBtn.setOnClickListener { activity?.onBackPressed() }
        return yogaBinding.root
    }

    private fun yogaDataSet() {
        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        yogaBinding.yogaRv.layoutManager = linearLayout
        yogaBinding.yogaRv.setHasFixedSize(true)
        yogaModel = setDataInYogaList()
        yogaAdapter = YogaAdapter(yogaModel, context)
        yogaBinding.yogaRv.adapter = yogaAdapter
    }

    private fun setDataInYogaList(): ArrayList<YogaModel> {
        val items: ArrayList<YogaModel> = ArrayList()
        items.apply {
            add(
                YogaModel(
                    R.drawable.view_profile_img_icon,
                    "9.4K Views",
                    "Paul Grand",
                    "Choose to lose: YOGA",
                    "1 Hr 30 Min", "Intermediate"
                )
            )
            add(
                YogaModel(
                    R.drawable.view_profile_img_icon,
                    "9.4K Views",
                    "Paul Grand",
                    "Choose to lose: YOGA",
                    "1 Hr 30 Min", "Intermediate"
                )
            )
            add(
                YogaModel(
                    R.drawable.view_profile_img_icon,
                    "9.4K Views",
                    "Paul Grand",
                    "Choose to lose: YOGA",
                    "1 Hr 30 Min", "Intermediate"
                )
            )
            add(
                YogaModel(
                    R.drawable.view_profile_img_icon,
                    "9.4K Views",
                    "Paul Grand",
                    "Choose to lose: YOGA",
                    "1 Hr 30 Min", "Intermediate"
                )
            )
            add(
                YogaModel(
                    R.drawable.view_profile_img_icon,
                    "9.4K Views",
                    "Paul Grand",
                    "Choose to lose: YOGA",
                    "1 Hr 30 Min", "Intermediate"
                )
            )
        }

        return items
    }

}