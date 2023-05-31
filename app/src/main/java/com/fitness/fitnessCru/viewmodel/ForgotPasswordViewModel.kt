package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.ForgotPasswordResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class ForgotPasswordViewModel(private val repository: Repository) : ViewModel() {
    val response = MutableLiveData<Response<ForgotPasswordResponse>>()

    fun sendForgotPasswordRequest(email: String) {
        viewModelScope.launch {
            response.value = repository.forgotPassword(email)
        }
    }
}