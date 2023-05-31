package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.UpdateGenderDobResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class UpdateGenderDobViewModel(private val repository: Repository) : ViewModel() {
    val response = MutableLiveData<Response<UpdateGenderDobResponse>>()

    fun updateGenderDob(dob: String, gender: String) {
        viewModelScope.launch {
            response.value = repository.updateGender(dob, gender)
        }
    }
}