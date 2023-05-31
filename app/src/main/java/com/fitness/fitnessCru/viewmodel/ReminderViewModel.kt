package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.body.ReminderBody
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.DeleteReminderResponse
import com.fitness.fitnessCru.response.GetAllReminderResponse
import com.fitness.fitnessCru.response.ReminderCommonResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class ReminderViewModel(private val repository: Repository) : ViewModel() {

    val allReminder = MediatorLiveData<Response<GetAllReminderResponse>>()
    val reminderById = MediatorLiveData<Response<ReminderCommonResponse>>()
    val deleteReminder = MediatorLiveData<Response<DeleteReminderResponse>>()
    val addReminder = MediatorLiveData<Response<ReminderCommonResponse>>()
    val updateReminder = MediatorLiveData<Response<ReminderCommonResponse>>()

    fun getAllReminder() {
        viewModelScope.launch {
            allReminder.value = repository.getAllReminder()
        }
    }

    fun deleteReminder(id: Int) {
        viewModelScope.launch {
            deleteReminder.value = repository.deleteReminder(id)
        }
    }

    fun addReminder(body: ReminderBody) {
        viewModelScope.launch {
            addReminder.value = repository.addReminder(body)
        }
    }

    fun updateReminder(body: ReminderBody) {
        viewModelScope.launch {
            updateReminder.value = repository.updateReminder(body)
        }
    }

}