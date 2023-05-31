package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.body.WorkoutFeedbackBody
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.WorkoutFeedbackResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class WorkoutFeedbackViewModel(private val repository: Repository) : ViewModel() {

    val response = MutableLiveData<Response<WorkoutFeedbackResponse>>()
    val sessionCompleted = MutableLiveData<Response<WorkoutFeedbackResponse>>()

    fun workoutFeedback(body: WorkoutFeedbackBody) {
        viewModelScope.launch {
            response.value = repository.workoutFeedback(body)
        }
    }

    fun sessionCompleted(workoutId: Int, sessionType: String) {
        viewModelScope.launch {
            sessionCompleted.value = repository.sessionCompleted(workoutId, sessionType)
        }
    }
}