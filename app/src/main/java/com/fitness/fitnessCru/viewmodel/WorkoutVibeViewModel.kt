package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.body.UpdateQuestionBody
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.UpdateQuestionResponse
import com.fitness.fitnessCru.response.WorkoutVibeResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class WorkoutVibeViewModel(private val repository: Repository) : ViewModel() {
    val response = MutableLiveData<Response<WorkoutVibeResponse>>()
    val response1 = MutableLiveData<Response<UpdateQuestionResponse>>()

    fun getWorkoutVibe() {
        viewModelScope.launch {
            response.value = repository.getWorkoutVibe()
        }
    }

    fun setQuesionareSelect(data: UpdateQuestionBody) {
        viewModelScope.launch {
            response1.value = repository.updateQuestion(data)
        }
    }
}