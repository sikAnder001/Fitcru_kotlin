package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.body.ChangePasswordBody
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.ChangePasswordResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class ChangePasswordViewModel(private val repository: Repository) : ViewModel() {

    val response = MutableLiveData<Response<ChangePasswordResponse>>()

    fun changePassword(changePasswordBody: ChangePasswordBody) {
        viewModelScope.launch {
            response.value = repository.changePassword(changePasswordBody)
        }
    }
}