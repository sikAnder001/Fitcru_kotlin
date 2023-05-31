package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.model.SNCProgramModel
import com.fitness.fitnessCru.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class SNCProgramViewModel(private val repository: Repository) : ViewModel() {
    val response = MutableLiveData<Response<SNCProgramModel>>()

    fun getDetail(id: String) {
        viewModelScope.launch {
            response.value = repository.getSncProgram(id)
        }
    }

}
