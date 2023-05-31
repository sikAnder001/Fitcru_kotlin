package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.body.ActivityBody
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.ActivityResponse
import com.fitness.fitnessCru.response.MySummaryResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class MySummaryViewModel(private val repository: Repository) : ViewModel() {

    val response = MutableLiveData<Response<MySummaryResponse>>()
    val response1 = MutableLiveData<Response<ActivityResponse>>()

    fun getSummary() {
        viewModelScope.launch {
            response.value = repository.getSummary()
        }
    }

    fun activityGoals(body: ActivityBody) {
        viewModelScope.launch {
            response1.value = repository.activityGoals(body)
        }
    }

}