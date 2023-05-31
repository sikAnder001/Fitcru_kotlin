package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.FitnessLevelResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class FitnessLevelViewModel(private val repository: Repository) : ViewModel() {

    val response = MutableLiveData<Response<FitnessLevelResponse>>()

    fun getFitnessLevel() {
        viewModelScope.launch { response.value = repository.getFitnessLevel() }
    }
}