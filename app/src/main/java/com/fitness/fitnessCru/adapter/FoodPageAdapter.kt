package com.fitness.fitnessCru.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fitness.fitnessCru.fragment.ChartsSummaryFragment
import com.fitness.fitnessCru.fragment.DesertFragment
import com.fitness.fitnessCru.fragment.IndianFragment
import com.fitness.fitnessCru.fragment.MembershipPlanFragment

class FoodPageAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IndianFragment()
//            1 -> ContinentalFragment(
            1 -> ChartsSummaryFragment()
            2 -> MembershipPlanFragment()
//            2 -> DrinksFragment()
            else -> DesertFragment()
        }
    }
}
