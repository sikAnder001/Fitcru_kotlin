package com.fitness.fitnessCru.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.activities.ViewProfileActivity
import com.fitness.fitnessCru.adapter.UpComingAppointmentAdapter
import com.fitness.fitnessCru.adapter.UpcomingCalendarAdapter
import com.fitness.fitnessCru.databinding.FragmentUpcomingBinding
import com.fitness.fitnessCru.interfaces.AppointmentInterface
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.utility.CalendarUtils
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.viewmodel.AppointmentViewModel
import com.fitness.fitnessCru.vm_factory.AppointmentVMFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class UpcomingFragment : Fragment(), UpcomingCalendarAdapter.OnItemListener {

    lateinit var binding: FragmentUpcomingBinding

    private lateinit var viewModel: AppointmentViewModel
    private lateinit var loading: CustomProgressLoading

    lateinit var calendarAdapter: UpcomingCalendarAdapter

    lateinit var upcomingAppointmentAdapter: UpComingAppointmentAdapter

    private lateinit var selectedDate: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpcomingBinding.inflate(inflater, container, false)

        val c: Calendar = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        selectedDate = df.format(c.getTime())

        val repository by lazy { Repository() }
        val vmFactory by lazy { AppointmentVMFactory(repository) }
        viewModel = ViewModelProvider(this, vmFactory)[AppointmentViewModel::class.java]
        loading = CustomProgressLoading(requireContext())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CalendarUtils.selectedDate = LocalDate.now()
            this.setMonthView()
        }

        appointmentList(selectedDate)

        binding.goBackBtn.setOnClickListener {
            startActivity(
                Intent(
                    requireContext()!!,
                    ViewProfileActivity::class.java
                )
            )
            requireActivity().finish()
        }
        return binding.root
    }

    private fun appointmentList(selectedDate: String) {
        upcomingAppointmentAdapter = UpComingAppointmentAdapter(requireActivity())
        binding.rvUpComing.adapter = upcomingAppointmentAdapter
        loading.showProgress()
        viewModel.upcomingAppointment(selectedDate)
        viewModel.upcomingAppointment.observe(viewLifecycleOwner) {
            loading.hideProgress()
            if (it.isSuccessful && it.body() != null) {
                val body = it.body()!!.ClientAppointment
                if (!body.isNullOrEmpty()) {
                    binding.toastTv.visibility = View.GONE
                    binding.rvUpComing.visibility = View.VISIBLE
                    upcomingAppointmentAdapter.setList(body)
                    upcomingAppointmentAdapter.notifyDataSetChanged()
                } else {
                    binding.toastTv.visibility = View.VISIBLE
                    binding.rvUpComing.visibility = View.GONE
                }
            }
        }
        upcomingAppointmentAdapter.setOnClick(object : AppointmentInterface {
            override fun onClick(Appointment_id: Int, coach_id: Int, time_slot_id: Int) {
                if (id != null)
                    callCancel(Appointment_id, coach_id, time_slot_id)
            }
        })
    }

    private fun callCancel(appoint_id: Int, coach_id: Int, time_slot_id: Int) {
        loading.showProgress()
        viewModel.cancelAppointment(appoint_id, coach_id, time_slot_id)
        viewModel.cancelAppointment.observe(viewLifecycleOwner) {
            loading.hideProgress()
            if (it.isSuccessful && it.body() != null) {
                Toast.makeText(context, it.body()!!.message, Toast.LENGTH_SHORT).show()
                appointmentList(selectedDate)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthView() {
        binding!!.monthYearTV.setText(CalendarUtils.selectedDate?.let {
            CalendarUtils.monthFromDate(
                it
            )
        })
        val daysInMonth: List<LocalDate?> =
            CalendarUtils.perfectDate(CalendarUtils.selectedDate!!)

        binding!!.yearTV.setText(CalendarUtils.selectedDate?.let {
            CalendarUtils.yearFromDate(
                it
            )
        })
        val year: List<LocalDate?> = CalendarUtils.daysInMonthArray(CalendarUtils.selectedDate!!)

        calendarAdapter = UpcomingCalendarAdapter(
            daysInMonth as List<LocalDate>,
            year as List<LocalDate>, this
        )
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL, false
        )
        binding!!.calendarRecyclerView.setLayoutManager(layoutManager)
        binding!!.calendarRecyclerView.setAdapter(calendarAdapter)
        binding!!.calendarRecyclerView.smoothScrollToPosition(8)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(position: Int, date: LocalDate?) {
        if (date != null) {
            CalendarUtils.selectedDate = date
            selectedDate = CalendarUtils.formattedDate(date)

            if (calendarAdapter != null) {
                loading.showProgress()
                appointmentList(selectedDate)
                calendarAdapter.notifyDataSetChanged()
                upcomingAppointmentAdapter.notifyDataSetChanged()
            }
        }
    }
}