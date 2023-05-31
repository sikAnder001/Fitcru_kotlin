package com.fitness.fitnessCru.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.YogaSearchAdapter
import com.fitness.fitnessCru.databinding.FragmentYogaSearchBinding
import com.fitness.fitnessCru.model.YogaSearchChildItem
import com.fitness.fitnessCru.model.YogaSearchModel

class YogaSearchFragment : Fragment() {

    private lateinit var yogaSearchBinding: FragmentYogaSearchBinding
    private lateinit var yogaSearchAdapter: YogaSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        yogaSearchBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_yoga_search, container, false)

        val yogaSearchChildDataModel: MutableList<YogaSearchChildItem> = ArrayList()
        yogaSearchChildDataModel.add(
            YogaSearchChildItem(
                R.drawable.view_profile_img_icon,
                "9.4K Views",
                "Paul Grand",
                "Choose to lose: YOGA",
                "1 Hr 30 Min", "Intermediate"
            )
        )
        yogaSearchChildDataModel.add(
            YogaSearchChildItem(
                R.drawable.view_profile_img_icon,
                "9.4K Views",
                "Paul Grand",
                "Choose to lose: YOGA",
                "1 Hr 30 Min", "Intermediate"
            )
        )
        yogaSearchChildDataModel.add(
            YogaSearchChildItem(
                R.drawable.view_profile_img_icon,
                "9.4K Views",
                "Paul Grand",
                "Choose to lose: YOGA",
                "1 Hr 30 Min", "Intermediate"
            )
        )
        yogaSearchChildDataModel.add(
            YogaSearchChildItem(
                R.drawable.view_profile_img_icon,
                "9.4K Views",
                "Paul Grand",
                "Choose to lose: YOGA",
                "1 Hr 30 Min", "Intermediate"
            )
        )

        yogaSearchChildDataModel.add(
            YogaSearchChildItem(
                R.drawable.view_profile_img_icon,
                "9.4K Views",
                "Paul Grand",
                "Choose to lose: YOGA",
                "1 Hr 30 Min", "Intermediate"
            )
        )


        val yogaSearchChildDataModel1: ArrayList<YogaSearchChildItem> = ArrayList()
        yogaSearchChildDataModel1.add(
            YogaSearchChildItem(
                R.drawable.view_profile_img_icon,
                "9.4K Views",
                "Paul Grand",
                "Choose to lose: YOGA",
                "1 Hr 30 Min", "Intermediate"
            )
        )

        yogaSearchChildDataModel1.add(
            YogaSearchChildItem(
                R.drawable.view_profile_img_icon,
                "9.4K Views",
                "Paul Grand",
                "Choose to lose: YOGA",
                "1 Hr 30 Min", "Intermediate"
            )
        )

        yogaSearchChildDataModel1.add(
            YogaSearchChildItem(
                R.drawable.view_profile_img_icon,
                "9.4K Views",
                "Paul Grand",
                "Choose to lose: YOGA",
                "1 Hr 30 Min", "Intermediate"
            )
        )

        yogaSearchChildDataModel1.add(
            YogaSearchChildItem(
                R.drawable.view_profile_img_icon,
                "9.4K Views",
                "Paul Grand",
                "Choose to lose: YOGA",
                "1 Hr 30 Min", "Intermediate"
            )
        )


        val yogaSearchChildDataModel2: ArrayList<YogaSearchChildItem> = ArrayList()
        yogaSearchChildDataModel2.add(
            YogaSearchChildItem(
                R.drawable.view_profile_img_icon,
                "9.4K Views",
                "Paul Grand",
                "Choose to lose: YOGA",
                "1 Hr 30 Min", "Intermediate"
            )
        )

        yogaSearchChildDataModel2.add(
            YogaSearchChildItem(
                R.drawable.view_profile_img_icon,
                "9.4K Views",
                "Paul Grand",
                "Choose to lose: YOGA",
                "1 Hr 30 Min", "Intermediate"
            )
        )

        yogaSearchChildDataModel2.add(
            YogaSearchChildItem(
                R.drawable.view_profile_img_icon,
                "9.4K Views",
                "Paul Grand",
                "Choose to lose: YOGA",
                "1 Hr 30 Min", "Intermediate"
            )
        )

        yogaSearchChildDataModel2.add(
            YogaSearchChildItem(
                R.drawable.view_profile_img_icon,
                "9.4K Views",
                "Paul Grand",
                "Choose to lose: YOGA",
                "1 Hr 30 Min", "Intermediate"
            )
        )


        val yogaSearchModel: MutableList<YogaSearchModel> = ArrayList()
        yogaSearchModel.add(
            YogaSearchModel(
                "New yoga programs",
                yogaSearchChildDataModel as ArrayList<YogaSearchChildItem>
            )
        )
        yogaSearchModel.add(
            YogaSearchModel(
                "30 min yoga session",
                yogaSearchChildDataModel1 as ArrayList<YogaSearchChildItem>
            )
        )
        yogaSearchModel.add(
            YogaSearchModel(
                "Fat burning yoga session",
                yogaSearchChildDataModel2 as ArrayList<YogaSearchChildItem>
            )
        )

        setYogaSearchRV(yogaSearchModel as ArrayList<YogaSearchModel>)

        return yogaSearchBinding.root
    }

    private fun setYogaSearchRV(yogaSearchModel: ArrayList<YogaSearchModel>) {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        yogaSearchBinding.yogaSearchRv.layoutManager = layoutManager
        yogaSearchAdapter = YogaSearchAdapter(yogaSearchModel, context)
        yogaSearchBinding.yogaSearchRv.adapter = yogaSearchAdapter
    }

}