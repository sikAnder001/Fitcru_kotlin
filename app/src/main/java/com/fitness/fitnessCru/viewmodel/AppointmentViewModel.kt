package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.body.BookAppointmentBody
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.AvailableTimeSlotResponse
import com.fitness.fitnessCru.response.BookAppointmentResponse
import com.fitness.fitnessCru.response.CoachListResponse
import com.fitness.fitnessCru.response.UpcomingAppointmentResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class AppointmentViewModel(private val repository: Repository) : ViewModel() {
    val timeSlotResponse = MutableLiveData<Response<AvailableTimeSlotResponse>>()
    val addBookAppointment = MutableLiveData<Response<BookAppointmentResponse>>()
    val upcomingAppointment = MutableLiveData<Response<UpcomingAppointmentResponse>>()
    val cancelAppointment = MutableLiveData<Response<BookAppointmentResponse>>()
    val coachList = MutableLiveData<Response<CoachListResponse>>()

    fun bookAppointment(body: BookAppointmentBody) {
        viewModelScope.launch {
            addBookAppointment.value = repository.bookAppointment(body)
        }
    }

    fun timeSlotResponse(id: Int, date: String) {
        viewModelScope.launch {
            timeSlotResponse.value = repository.availableTimeSlot(id, date)
        }
    }

    fun upcomingAppointment(selectedDate: String) {
        viewModelScope.launch {
            upcomingAppointment.value = repository.upcomingAppointment(selectedDate)
        }
    }

    fun cancelAppointment(id: Int, coach_id: Int, time_slot_id: Int) {
        viewModelScope.launch {
            cancelAppointment.value = repository.cancelAppointment(id, coach_id, time_slot_id)
        }
    }

    fun getCoachList() {
        viewModelScope.launch {
            coachList.value = repository.getCoachList()
        }
    }
}