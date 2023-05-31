package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.GetQuestionsByUserIdResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class GetQuestionsByUserIdViewModel(private val repository: Repository) : ViewModel() {

    val response = MutableLiveData<Response<GetQuestionsByUserIdResponse>>()

    fun getQuestionByUserID() {
        viewModelScope.launch {
            response.value = repository.getQuestionByUserID()
        }
    }
}