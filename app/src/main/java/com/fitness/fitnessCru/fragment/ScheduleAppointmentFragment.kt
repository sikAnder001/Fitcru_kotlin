package com.fitness.fitnessCru.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fitness.fitnessCru.adapter.AppointmentPageAdapter
import com.fitness.fitnessCru.databinding.FragmentScheduleAppointmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class ScheduleAppointmentFragment : Fragment() {

    private lateinit var appointmentBinding: FragmentScheduleAppointmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appointmentBinding = FragmentScheduleAppointmentBinding.inflate(inflater, container, false)

        appointmentBinding.apply {
            viewPager.adapter = AppointmentPageAdapter(this@ScheduleAppointmentFragment)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = arrayOf("Schedule Appointment", "Upcoming Appointment")[position]
            }.attach()
        }
        appointmentBinding.goBackBtn.setOnClickListener { requireActivity().onBackPressed() }
        return appointmentBinding.root
    }

}