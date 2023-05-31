package com.fitness.fitnessCru.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.adapter.TodaySessionAdapter
import com.fitness.fitnessCru.databinding.FragmentWTodaySessionBinding
import com.fitness.fitnessCru.model.TodaySessionModel


class WTodaySessionFragment : Fragment() {

    private lateinit var todaySessionBinding: FragmentWTodaySessionBinding
    private lateinit var todaySessionAdapter: TodaySessionAdapter
    private lateinit var todaySessionModel: ArrayList<TodaySessionModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        todaySessionBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_w_today_session, container, false)
        todaySessionRVSet()
        return todaySessionBinding.root
    }

    private fun todaySessionRVSet() {
        val linearLayout = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        todaySessionBinding.todaySessionRv.layoutManager = linearLayout
        todaySessionBinding.todaySessionRv.setHasFixedSize(true)
        todaySessionModel = setDataInTodaySessionList()
        todaySessionAdapter = TodaySessionAdapter(todaySessionModel, context)
        todaySessionBinding.todaySessionRv.adapter = todaySessionAdapter
    }

    private fun setDataInTodaySessionList(): ArrayList<TodaySessionModel> {
        val items: ArrayList<TodaySessionModel> = ArrayList()
        items.apply {
            add(
                TodaySessionModel(
                    "9.4K Views",
                    R.drawable.place_holder,
                    "6 Sessions",
                    "S&C Lower Body",
                    "1 Hr 30 Min",
                    "448 Cal", "Intermediate"
                )
            )
            add(
                TodaySessionModel(
                    "9.4K Views",
                    R.drawable.place_holder,
                    "6 Sessions",
                    "S&C Lower Body",
                    "1 Hr 30 Min",
                    "448 Cal", "Intermediate"
                )
            )
            add(
                TodaySessionModel(
                    "9.4K Views",
                    R.drawable.place_holder,
                    "6 Sessions",
                    "S&C Lower Body",
                    "1 Hr 30 Min",
                    "448 Cal", "Intermediate"
                )
            )
            add(
                TodaySessionModel(
                    "9.4K Views",
                    R.drawable.place_holder,
                    "6 Sessions",
                    "S&C Lower Body",
                    "1 Hr 30 Min",
                    "448 Cal", "Intermediate"
                )
            )
            add(
                TodaySessionModel(
                    "9.4K Views",
                    R.drawable.place_holder,
                    "6 Sessions",
                    "S&C Lower Body",
                    "1 Hr 30 Min",
                    "448 Cal", "Intermediate"
                )
            )

        }

        return items
    }

}