package com.fitness.fitnessCru.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.fitness.fitnessCru.R
import com.fitness.fitnessCru.activities.ViewProfileActivity
import com.fitness.fitnessCru.adapter.WorkoutPageAdapter
import com.fitness.fitnessCru.databinding.FragmentWorkoutBinding
import com.fitness.fitnessCru.utility.Session
import com.google.android.material.tabs.TabLayoutMediator

class Workout : Fragment() {

    lateinit var workoutBinding: FragmentWorkoutBinding

    private var position = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workoutBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_workout, container, false)

        try {
            position = requireArguments().getInt("position", 0)
        } catch (e: Exception) {
            position = 0
        }

        workoutBinding.apply {
            viewPager.adapter = WorkoutPageAdapter(this@Workout)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = arrayOf("Programs", "Studio", "Habits", "Today’s Session")[position]
            }.attach()

            if (position != 0)
                tabLayout.getTabAt(position)!!.select()
            viewPager.isUserInputEnabled = false

            var userDetail = Session.getUserDetails()

            if (userDetail.name == null) {
                if (userDetail.email != null) {
                    var n = userDetail.email[0].toString().uppercase()
                    gobackbtn.text = n
                } else {
                    gobackbtn.visibility = View.GONE
                    placeholder.visibility = View.VISIBLE
                }
            } else {
                var n = userDetail.name[0].toString().uppercase()
                gobackbtn.text = n
            }

            placeholder.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext()!!,
                        ViewProfileActivity::class.java
                    )
                )
            }
            gobackbtn.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext()!!,
                        ViewProfileActivity::class.java
                    )
                )
            }
        }

        return workoutBinding.root
    }


}