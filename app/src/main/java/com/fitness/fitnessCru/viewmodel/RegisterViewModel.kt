package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.RegisterResponse
import retrofit2.Response

class RegisterViewModel(private val repository: Repository) : ViewModel() {

    val response = MutableLiveData<Response<RegisterResponse>>()

}