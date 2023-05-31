package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.OtpResponse
import com.fitness.fitnessCru.response.ResendOtpResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class OtpViewModel(private val repository: Repository) : ViewModel() {
    val response = MutableLiveData<Response<OtpResponse>>()
    val response1 = MutableLiveData<Response<ResendOtpResponse>>()

    fun otpFun(auth: String, otp: String, quickBloxId: String?) {
        viewModelScope.launch {
            response.value = repository.otpFun(auth, otp, quickBloxId)
        }
    }

    fun resendotpFun(auth: String, number: String) {
        viewModelScope.launch {
            response1.value = repository.resendOtpFun(auth, number)
        }
    }

}
