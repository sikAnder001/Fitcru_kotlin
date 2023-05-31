package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.body.ConsumedAllMealBody
import com.fitness.fitnessCru.body.ConsumedMealBody
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.*
import kotlinx.coroutines.launch
import retrofit2.Response

class DashBoardViewModel(private val repository: Repository) : ViewModel() {

    val response = MutableLiveData<Response<DashBoardResponse>>()
    val response1 = MutableLiveData<Response<ConsumedMealResponse>>()
    val mealdetail = MutableLiveData<Response<MealTypeDetailResponse>>()
    val notConsumed = MutableLiveData<Response<NotConsumedMealResponse>>()
    val consumeAll = MutableLiveData<Response<ConsumedAllMealResponse>>()
    val uncheckConsumeAll = MutableLiveData<Response<NotAllConsumedMealResponse>>()

    fun getDashBoard(date: String) {
        viewModelScope.launch {
            response.value = repository.getDashBoard(date)
        }
    }

    fun consumedMeal(body: ConsumedMealBody) {
        viewModelScope.launch {
            response1.value = repository.consumedMeal(body)
        }
    }

    fun notConsumedMeal(body: Int) {
        viewModelScope.launch {
            notConsumed.value = repository.notConsumedMeal(body)
        }
    }

    fun getMealDetail(id: Int, date: String, time: String) {
        viewModelScope.launch {
            mealdetail.value = repository.getMealDetail(id, date, time)
        }
    }

    fun consumedAllMeal(foodsList: ConsumedAllMealBody) {
        viewModelScope.launch {
            consumeAll.value = repository.consumedAllMeal(foodsList)
        }
    }

    fun removeAllConsume(ids: String) {
        viewModelScope.launch {
            uncheckConsumeAll.value = repository.removeAllConsume(ids)
        }
    }
}