package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.HowActiveYouResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class HowActiveYouViewModel(private val repository: Repository) : ViewModel() {
    val response = MutableLiveData<Response<HowActiveYouResponse>>()

    fun getHowActiveYou() {
        viewModelScope.launch {
            response.value = repository.howActiveYou()
        }
    }
}