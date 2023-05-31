package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.model.ExerciseByWorkoutModel
import com.fitness.fitnessCru.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class WorkoutExersiseViewModel(private val repository: Repository) : ViewModel() {
    val response = MutableLiveData<Response<ExerciseByWorkoutModel>>()

    fun getExercise(id: Int) {
        viewModelScope.launch {
            response.value = repository.getExercise(id)
        }
    }
}
