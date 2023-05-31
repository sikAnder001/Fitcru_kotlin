package com.fitness.fitnessCru.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fitness.fitnessCru.fragment.CCoachFragment
import com.fitness.fitnessCru.fragment.CMentalHealthFragment
import com.fitness.fitnessCru.response.CoachCategoryResponse

class CoachingPageAdapter(fa: Fragment, val response: CoachCategoryResponse?) :
    FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return response!!.data.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CCoachFragment(response!!.data[position].id, response!!.data[position].name)
            1 -> CCoachFragment(response!!.data[position].id, response!!.data[position].name)
            2 -> CMentalHealthFragment(response!!.data[position].id, response!!.data[position].name)
            else -> CCoachFragment(response!!.data[position].id, response!!.data[position].name)
        }
    }
}