package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.body.SignInBody
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.SignInResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class SignInViewModel(private val repository: Repository) : ViewModel() {

    val response = MutableLiveData<Response<SignInResponse>>()

    fun sendSignInRequest(signinBody: SignInBody) {
        viewModelScope.launch {
            response.value = repository.doRegister(signinBody)
        }
    }
}