package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.EmailCodeResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class EmailVerfiryViewModel(private val repository: Repository) : ViewModel() {
    val response = MutableLiveData<Response<EmailCodeResponse>>()

    fun emailCodeFun(auth: String, code: String) {
        viewModelScope.launch {
            response.value = repository.emailCodeFun(auth, code)
        }
    }

}
