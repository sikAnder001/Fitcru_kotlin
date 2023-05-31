package com.fitness.fitnessCru.viewmodel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.MobileLoginResponse
import com.fitness.fitnessCru.response.StoreQuickbloxResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class MobileLoginViewModel(private val repository: Repository) : ViewModel() {

    val response = MutableLiveData<Response<MobileLoginResponse>>()
    val activateNumber = MutableLiveData<Response<MobileLoginResponse>>()
    val responseStoreId = MutableLiveData<Response<StoreQuickbloxResponse>>()

    fun sendLoginRequest(
        mobile: String,
        deviceToken: String,
        deviceId: String,
        deviceType: Int,
        quickBloxId: String
    ) {
        viewModelScope.launch {
            response.value =
                repository.login(mobile, deviceToken, deviceId, deviceType, quickBloxId)
        }
    }

    fun activateNumberAccount(number: String) {
        viewModelScope.launch {
            activateNumber.value = repository.activateNumberAccount(number)
        }
    }

    fun storequickbloxid(
        viewLifecycleOwner: LifecycleOwner,
        context: Context,
        quickBloxId: String,
        user_id: String
    ) {
        viewModelScope.launch {
            responseStoreId.value =
                repository.storequickbloxid(quickBloxId, user_id)

        }
    }
}