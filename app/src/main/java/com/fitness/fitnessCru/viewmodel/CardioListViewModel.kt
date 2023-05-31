package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.CardioListResponse
import retrofit2.Response

class CardioListViewModel(private val repository: Repository) : ViewModel() {
    val response = MutableLiveData<Response<CardioListResponse>>()

}