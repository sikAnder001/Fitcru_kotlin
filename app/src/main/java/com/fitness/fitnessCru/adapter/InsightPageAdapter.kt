package com.fitness.fitnessCru.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fitness.fitnessCru.fragment.ChartsSummaryFragment
import com.fitness.fitnessCru.fragment.MembershipPlanFragment
import com.fitness.fitnessCru.fragment.MyRemindersFragment

class InsightPageAdapter(fa: Fragment) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MembershipPlanFragment()
            1 -> ChartsSummaryFragment()
            else -> MyRemindersFragment()
        }
    }
}
