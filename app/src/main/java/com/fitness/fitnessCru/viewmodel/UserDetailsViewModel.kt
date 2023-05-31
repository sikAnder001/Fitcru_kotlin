package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.body.UpdateQuestionBody2
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.*
import kotlinx.coroutines.launch
import retrofit2.Response

class UserDetailsViewModel(private val repository: Repository) : ViewModel() {

    val response = MutableLiveData<Response<UserDetailsResponse>>()

    val response2 =
        MutableLiveData<Response</*UpdateQuestionResponse*/UpdateGetQuestionByUserIDResponse>>()

    val foodPref = MutableLiveData<Response<DietTypeResponse>>()

    val fitnessLevel = MutableLiveData<Response<FitnessLevelResponse>>()

    val dailyActivity = MutableLiveData<Response<HowActiveYouResponse>>()

    val workoutVibe = MutableLiveData<Response<WhatBringsYouResponse>>()

    val yourGoal = MutableLiveData<Response<WorkoutVibeResponse>>()


    fun getUserDetails() {
        viewModelScope.launch {
            response.value = repository.getUserDetails()
        }
    }

    fun setQuestionnaireSelect(data: UpdateQuestionBody2) {
        viewModelScope.launch {
            response2.value = repository.updateQuestion2(data)
        }
    }

    fun getFoodPref() {
        viewModelScope.launch { foodPref.value = repository.getDietType() }
    }

    fun getFitnessLevel() {
        viewModelScope.launch { fitnessLevel.value = repository.getFitnessLevel() }
    }

    fun getDailyActivity() {
        viewModelScope.launch {
            dailyActivity.value = repository.howActiveYou()
        }
    }

    fun getWorkoutVibe() {
        viewModelScope.launch {
            workoutVibe.value = repository.whatBringsYou()
        }
    }

    fun getYourGoal() {
        viewModelScope.launch {
            yourGoal.value = repository.getWorkoutVibe()
        }
    }
}