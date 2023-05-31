package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.model.MealsPojo
import com.fitness.fitnessCru.repository.Repository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class NutritionDashboardViewModel(private val repository: Repository) : ViewModel() {

    val response = MutableLiveData<Response<MealsPojo>>()
    val downloadPDF = MutableLiveData<Response<ResponseBody>>()

    fun getNutritionDetails(date: String) {
        viewModelScope.launch {
            response.value = repository.getNutritionDashboard(date)
        }
    }

    fun downloadPDF(selectedDate: String) {
        viewModelScope.launch {
            downloadPDF.value = repository.downloadPDF(selectedDate)
        }
    }
}