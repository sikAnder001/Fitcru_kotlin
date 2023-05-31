package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.TrendingOfferResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class TrendingOfferViewModel(private val repository: Repository) : ViewModel() {
    val response = MutableLiveData<Response<TrendingOfferResponse>>()

    fun getTrendingOffers(id: Int) {
        viewModelScope.launch {
            response.value = repository.getTrendingOffers(id)
        }
    }
}