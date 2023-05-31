package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.body.LastMessage
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.ChatListResponse
import com.fitness.fitnessCru.response.LastMessageResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class ChatViewModel(private val repository: Repository) : ViewModel() {

    val chatList = MutableLiveData<Response<ChatListResponse>>()
    val lastMessages = MutableLiveData<Response<LastMessageResponse>>()

    fun getChatList() {
        viewModelScope.launch {
            chatList.value = repository.chatList()
        }
    }

    fun lastMessage(lastMessage: LastMessage) {
        viewModelScope.launch {
            lastMessages.value = repository.lastMessage(lastMessage)
        }
    }
}