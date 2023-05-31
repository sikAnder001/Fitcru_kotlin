package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.StudioDetailsResponse
import com.fitness.fitnessCru.response.WorkoutByFitnessResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class StudioDetailsViewModel(private val repository: Repository) : ViewModel() {

    val response = MutableLiveData<Response<StudioDetailsResponse>>()

    val getWorkoutByFitness = MutableLiveData<Response<WorkoutByFitnessResponse>>()

    fun getStudioDetails(studioId: Int) {
        viewModelScope.launch {
            response.value = repository.getStudioDetails(studioId)
        }
    }

    fun getWorkoutByFitness(levelId: Int) {
        viewModelScope.launch {
            getWorkoutByFitness.value = repository.getWorkoutByFitness(levelId)
        }
    }
}