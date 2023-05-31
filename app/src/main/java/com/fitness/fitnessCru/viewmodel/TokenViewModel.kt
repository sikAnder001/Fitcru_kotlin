package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.TokenResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class TokenViewModel(private val repository: Repository) : ViewModel() {

    val response = MutableLiveData<Response<TokenResponse>>()

    fun getToken() {
        viewModelScope.launch {
            response.value = repository.token()
        }
    }
}