package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.WorkoutDetailResponse
import com.fitness.fitnessCru.response.WorkoutProgramResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class WorkoutProgramViewModel(private val repository: Repository) : ViewModel() {

    val response = MutableLiveData<Response<WorkoutProgramResponse>>()
    val response1 = MutableLiveData<Response<WorkoutDetailResponse>>()

    fun getProgram() {
        viewModelScope.launch {
            response.value = repository.getProgram()
        }
    }

    fun getWorkoutDetail(id: Int?) {
        viewModelScope.launch {
            response1.value = repository.getWorkoutDetail(id)
        }
    }


}