package com.fitness.fitnessCru.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitness.fitnessCru.activities.SetupAllActivity
import com.fitness.fitnessCru.adapter.AppointmentCalendarAdapter
import com.fitness.fitnessCru.adapter.SlotsAdapter
import com.fitness.fitnessCru.adapter.UpcomingCalendarAdapter
import com.fitness.fitnessCru.body.BookAppointmentBody
import com.fitness.fitnessCru.body.CoachData
import com.fitness.fitnessCru.databinding.FragmentAppointmentBinding
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.AvailableTimeSlotResponse
import com.fitness.fitnessCru.utility.CalendarUtils
import com.fitness.fitnessCru.utility.Constants
import com.fitness.fitnessCru.utility.CustomProgressLoading
import com.fitness.fitnessCru.utility.Util
import com.fitness.fitnessCru.viewmodel.AppointmentViewModel
import com.fitness.fitnessCru.vm_factory.AppointmentVMFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class AppointmentFragment : Fragment(), AppointmentCalendarAdapter.OnItemListener,
    UpcomingCalendarAdapter.OnItemListener {

    private lateinit var appointmentBinding: FragmentAppointmentBinding
    private lateinit var calendarAdapter: AppointmentCalendarAdapter
    private lateinit var selectedDate: String
    private lateinit var viewModel: AppointmentViewModel
    private lateinit var loading: CustomProgressLoading
    private var coachTimeSlotId: Int = 0
    private var appointmentDate: String = ""
    private var appointmentDayName: String = ""
    private var coachId: Int = 0
    private lateinit var bundleData: CoachData

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appointmentBinding = FragmentAppointmentBinding.inflate(inflater, container, false)

        bundleData = arguments?.getSerializable("data") as CoachData
        coachId = bundleData.id

        val repository by lazy { Repository() }
        val vmFactory by lazy { AppointmentVMFactory(repository) }
        viewModel = ViewModelProvider(this, vmFactory)[AppointmentViewModel::class.java]
        loading = CustomProgressLoading(requireContext())
        val c: Calendar = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        selectedDate = df.format(c.time)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CalendarUtils.selectedDate = LocalDate.now()
            this.setMonthView()
        }
        availableSlots()

        booking()

        appointmentBinding.backBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        return appointmentBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthView() {
        appointmentBinding!!.monthYearTV.text = CalendarUtils.selectedDate?.let {
            CalendarUtils.monthFromDate(
                it
            )
        }
        val daysInMonth: List<LocalDate?> =
            CalendarUtils.perfectDate(CalendarUtils.selectedDate!!)

        val year: List<LocalDate?> = CalendarUtils.daysInMonthArray(CalendarUtils.selectedDate!!)

        calendarAdapter = AppointmentCalendarAdapter(
            daysInMonth as List<LocalDate>,
            year as List<LocalDate>, this
        )
        var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL, false
        )
        appointmentBinding!!.calendarRecyclerView2.apply {
            layoutManager = layoutManager
            adapter = calendarAdapter
        }
        Util.recyclerSlideIssueFix(appointmentBinding.calendarRecyclerView2)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(position: Int, date: LocalDate?) {
        if (date != null) {
            CalendarUtils.selectedDate = date
            appointmentDate = CalendarUtils.formattedDate(date)
            //val day: String = CalendarUtils.formattedDate1(date)
            if (calendarAdapter != null) {
                appointmentDayName = date.dayOfWeek.toString()
//                appointmentDate = "${date.year}-${date.month}-${date.dayOfMonth}"
                viewModel.timeSlotResponse(coachId, appointmentDate)
                calendarAdapter.notifyDataSetChanged()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun availableSlots() {
        if (appointmentDate == "") {
            appointmentDate = CalendarUtils.currentDate().toString()
        }
        val slotAdapter = SlotsAdapter(context, object : SlotsAdapter.AvailabilityInterface {
            override fun onClick(slotId: Int) {
                coachTimeSlotId = slotId
            }
        })
        appointmentBinding.slotsAvailableRv.adapter = slotAdapter
        loading.showProgress()
        viewModel.timeSlotResponse(coachId, appointmentDate)
        viewModel.timeSlotResponse.observe(viewLifecycleOwner) {
            loading.hideProgress()
            if (it.isSuccessful && it.body() != null) {
                val body = it.body()!!.data
                appointmentBinding.apply {

                    if (!body.available_time_slots.isNullOrEmpty()) {
                        slotsAvailableRv.visibility = View.VISIBLE
                        toastTv.visibility = View.GONE
                        slotAdapter.setList(body.available_time_slots)
                    } else {
                        slotsAvailableRv.visibility = View.GONE
                        toastTv.visibility = View.VISIBLE
                    }

                    tvCoachName.text = body.coach_details.coach_name

                    if (bundleData.location != null && bundleData.location!!.isNotEmpty()) {
                        locationView.visibility = View.VISIBLE
                        location.text = bundleData.location
                    } else {
                        locationView.visibility = View.GONE
                    }

                    /*if (bundleData.years_experience != null) {
                        tvDuration.text = "${bundleData.years_experience} Years of experience"
                    } else {
                        tvDuration.text = "${0} Years of experience"
                    }*/

                    if (bundleData.clients_experience!! > 100) {
                        tvClients.text = "Coached ${bundleData.clients_experience}+ clients"
                    } else {
                        tvClients.text = "Coached ${bundleData.clients_experience} clients"
                    }

                    Util.loadImage(requireContext(), imageView, body.coach_details.image_url)

                    tvCoachName.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putInt("coachId", body.coach_details.id)
                        val intent = Intent(context, SetupAllActivity::class.java)
                        intent.putExtra(Constants.DESTINATION, Constants.COACH_PROFILE)
                        intent.putExtras(bundle)
                        requireContext().startActivity(intent)
                    }
                }

            }
        }
    }

    private fun isValid(): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (coachTimeSlotId == 0)
            result = Pair(false, "Please select time slot")
        else if (appointmentDate.isEmpty())
            result = Pair(false, "Please select date")
        return result
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun booking() {
        appointmentBinding.bookingBtn.setOnClickListener {
            if (isValid().first) {
                loading.showProgress()
                viewModel.bookAppointment(
                    BookAppointmentBody(
                        coachTimeSlotId,
                        appointmentDate,
                        appointmentDayName,
                        coachId
                    )
                )
            } else Util.toast(requireContext(), isValid().second)
        }
        if (appointmentDate == "") {
            appointmentDate = CalendarUtils.currentDate().toString()
        }
        viewModel.addBookAppointment.observe(viewLifecycleOwner) {
            loading.hideProgress()
            // viewModel.timeSlotResponse(coachId, appointmentDate)
            if (it.isSuccessful && it.body() != null) {
                Util.toast(requireContext(), it.body()!!.message)
                requireActivity().onBackPressed()
            } else if (!it.isSuccessful && it.errorBody() != null) {
                Util.error(it.errorBody(), AvailableTimeSlotResponse::class.java).message
            } else "Something went wrong"
        }
    }

}