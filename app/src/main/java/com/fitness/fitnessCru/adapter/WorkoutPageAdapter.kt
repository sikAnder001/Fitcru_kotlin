package com.fitness.fitnessCru.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fitness.fitnessCru.fragment.WHabitsFragment
import com.fitness.fitnessCru.fragment.WProgramsFragment
import com.fitness.fitnessCru.fragment.WStudioFragment
import com.fitness.fitnessCru.fragment.WTodaySessionFragment

class WorkoutPageAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WProgramsFragment()
            1 -> WStudioFragment()
            2 -> WHabitsFragment()
            else -> WTodaySessionFragment()
        }
    }
}