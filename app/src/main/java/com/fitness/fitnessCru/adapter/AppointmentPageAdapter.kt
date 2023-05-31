package com.fitness.fitnessCru.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fitness.fitnessCru.fragment.CoachListFragment
import com.fitness.fitnessCru.fragment.UpcomingFragment

class AppointmentPageAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CoachListFragment()/*AppointmentFragment()*/
            else -> UpcomingFragment()
        }
    }
}
