package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.helper.ResponseModel
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.NotificationListResponse
import com.fitness.fitnessCru.response.ReadNotificationResponse
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Response

class NotificationsViewModel(private val repository: Repository) : ViewModel() {
    val response = MutableLiveData<Response<NotificationListResponse>>()
    val readResponse = MutableLiveData<Response<ReadNotificationResponse>>()
    val watchResponse = MutableLiveData<Response<ResponseModel>>()

    fun notificationList() {
        viewModelScope.launch {
            response.value = repository.notificationList()
        }
    }

    fun readNotification(id: Int) {
        viewModelScope.launch {
            readResponse.value = repository.readNotification(id)
        }
    }

    fun watchSync(map: MutableMap<String, RequestBody>) {
        viewModelScope.launch {
            watchResponse.value = repository.watchSync(map)
        }
    }

}