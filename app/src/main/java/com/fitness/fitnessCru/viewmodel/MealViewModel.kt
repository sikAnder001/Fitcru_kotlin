package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.body.MealBody
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.*
import kotlinx.coroutines.launch
import retrofit2.Response

class MealViewModel(private val repository: Repository) : ViewModel() {

    val responseMealType = MutableLiveData<Response<MealTypeResponse>>()
    val mealByIdResponse = MutableLiveData<Response<MealByIdResponse>>()
    val addMealResponse = MutableLiveData<Response<AddMealResponse>>()
    val updateMealResponse = MutableLiveData<Response<UpdateMealResponse>>()
    val foodListResponse = MutableLiveData<Response<FoodListResposne>>()
    val deleteFood = MutableLiveData<Response<DeleteFoodResponse>>()

    fun responseMealType(selectedDate: String) {
        viewModelScope.launch {
            responseMealType.value = repository.mealType(selectedDate)
        }
    }

    fun mealByIdResponse(id: Int, mDate: String, mTime: String) {
        viewModelScope.launch {
            mealByIdResponse.value = repository.mealById(id, mDate, mTime)
        }
    }

    fun addMealResponse(body: MealBody) {
        viewModelScope.launch {
            addMealResponse.value = repository.addMeal(body)
        }
    }

    fun updateMealResponse(body: MealBody) {
        viewModelScope.launch {
            updateMealResponse.value = repository.updateMeal(body)
        }
    }

    fun foodListResponse(search: String) {
        viewModelScope.launch {
            foodListResponse.value = repository.getFoods(search)
        }
    }

    fun deleteFood(i: Int) {
        viewModelScope.launch {
            deleteFood.value = repository.deleteFood(i)
        }
    }
}