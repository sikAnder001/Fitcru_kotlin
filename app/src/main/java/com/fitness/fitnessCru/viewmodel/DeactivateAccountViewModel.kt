package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.ChangePasswordResponse
import com.fitness.fitnessCru.response.LogoutResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class DeactivateAccountViewModel(private val repository: Repository) : ViewModel() {

    val deactivateAccount = MutableLiveData<Response<ChangePasswordResponse>>()

    val logout = MutableLiveData<Response<LogoutResponse>>()

    val delete = MutableLiveData<Response<LogoutResponse>>()


    fun deactivateAccount() {
        viewModelScope.launch {
            deactivateAccount.value = repository.deactivateAccount()
        }
    }

    fun logout() {
        viewModelScope.launch {
            logout.value = repository.logout()
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            delete.value = repository.deleteAccount()
        }
    }
}