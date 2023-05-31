package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.body.UpdateUserDetailsBody
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.UserDetailsResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class UpdateUserDetailsViewModel(private val repository: Repository) : ViewModel() {

    val response = MutableLiveData<Response<UserDetailsResponse>>()

    fun updateUserDetails(updateUserDetailsBody: UpdateUserDetailsBody) {
        viewModelScope.launch {
            response.value = repository.updateUserDetails(updateUserDetailsBody)
        }
    }
}