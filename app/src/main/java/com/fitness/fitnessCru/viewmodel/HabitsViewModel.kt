package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.HabitsResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class HabitsViewModel(private val repository: Repository) : ViewModel() {
    val response = MutableLiveData<Response<HabitsResponse>>()

    fun getHabitsList() {
        viewModelScope.launch {
            response.value = repository.getHabitsList()
        }
    }
}