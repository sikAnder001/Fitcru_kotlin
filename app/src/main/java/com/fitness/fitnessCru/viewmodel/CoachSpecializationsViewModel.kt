package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.body.GetCoachListBody
import com.fitness.fitnessCru.body.GetCoachListBody2
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.GetCoachListResponse
import com.fitness.fitnessCru.response.GetCoachSpecializationsResponse
import com.fitness.fitnessCru.response.UserSelectPlanResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class CoachSpecializationsViewModel(private val repository: Repository) : ViewModel() {

    val coachSpecializations = MutableLiveData<Response<GetCoachSpecializationsResponse>>()
    val coachList = MutableLiveData<Response<GetCoachListResponse>>()
    val userSelectPlan = MutableLiveData<Response<UserSelectPlanResponse>>()

    fun getCoachSpecializations() {
        viewModelScope.launch {
            coachSpecializations.value =
                repository.getCoachSpecializations()
        }
    }

    fun coachWithSpecialization(body: GetCoachListBody) {
        viewModelScope.launch {
            coachList.value = repository.coachWithSpecialization(body)
        }
    }

    fun coachWithoutSpecialization(body: GetCoachListBody2) {
        viewModelScope.launch {
            coachList.value = repository.coachWithoutSpecialization(body)
        }
    }

}