package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.DietTypeResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class DietTypeViewModel(private val repository: Repository) : ViewModel() {

    val response = MutableLiveData<Response<DietTypeResponse>>()

    fun getDietType() {
        viewModelScope.launch { response.value = repository.getDietType() }
    }
}