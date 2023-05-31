package com.fitness.fitnessCru.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fitness.fitnessCru.fragment.*

class ProfilePageAdapter(fa: FragmentActivity, val answer: String) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return if (answer == "yes")
            5 else 4
    }

    override fun createFragment(position: Int): Fragment {
        return if (answer == "yes") {
            when (position) {
                0 -> BasicFragment()
                1 -> EHealthFragment()
                2 -> HealthQuestionnaireFragment()
                3 -> ESaveAddressFragment()
                else -> ChangePasswordFragment()
            }
        } else {
            when (position) {
                0 -> BasicFragment()
                1 -> EHealthFragment()
                2 -> ESaveAddressFragment()
                else -> ChangePasswordFragment()
            }
        }
    }
}