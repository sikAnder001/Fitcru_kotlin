package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.CongratulationResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class CongratulationsViewModel(val repository: Repository) : ViewModel() {
    var response = MutableLiveData<Response<CongratulationResponse>>()

    fun getCongrats() {
        viewModelScope.launch {
            response.value = repository.getCongrats()
        }
    }
}