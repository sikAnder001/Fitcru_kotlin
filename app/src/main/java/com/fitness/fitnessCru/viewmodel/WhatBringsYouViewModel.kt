package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.WhatBringsYouResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class WhatBringsYouViewModel(private val repository: Repository) : ViewModel() {

    val response = MutableLiveData<Response<WhatBringsYouResponse>>()

    fun getWhatBringsYou() {
        viewModelScope.launch {
            response.value = repository.whatBringsYou()
        }
    }


}