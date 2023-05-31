package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.body.HealthQuestionBody
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.GetHealthQuestionnaire
import com.fitness.fitnessCru.response.HealthQuestionResponse
import com.fitness.fitnessCru.response.ImageUploadResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response

class HealthQuestionViewModel(private val repository: Repository) : ViewModel() {
    val response = MutableLiveData<Response<HealthQuestionResponse>>()
    val responseOne = MutableLiveData<Response<GetHealthQuestionnaire>>()
    val updateHealth = MutableLiveData<Response<HealthQuestionResponse>>()
    val image = MutableLiveData<Response<ImageUploadResponse>>()

    fun healthQuestion(body: HealthQuestionBody) {
        viewModelScope.launch {
            response.value = repository.healthQuestion(body)
        }
    }

    fun getHealthQuestion() {
        viewModelScope.launch {
            responseOne.value = repository.getHealthQuestionnaire()
        }
    }

    fun uploadImageQuestionnaire(body: MultipartBody) {
        viewModelScope.launch {
            image.value = repository.uploadImageQuestionnaire(body)
        }
    }
}