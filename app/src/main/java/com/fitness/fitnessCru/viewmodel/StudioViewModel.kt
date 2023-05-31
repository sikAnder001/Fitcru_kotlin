package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.StudioResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class StudioViewModel(private val repository: Repository) : ViewModel() {

    val response = MutableLiveData<Response<StudioResponse>>()

    fun getStudio() {
        viewModelScope.launch {
            response.value = repository.getStudio()
        }
    }
}