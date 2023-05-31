package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.LogoutResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class LogoutViewModel(val repository: Repository) : ViewModel() {

    val response = MutableLiveData<Response<LogoutResponse>>()

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}