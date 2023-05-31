package com.fitness.fitnessCru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.fitnessCru.body.AddressBody
import com.fitness.fitnessCru.repository.Repository
import com.fitness.fitnessCru.response.*
import kotlinx.coroutines.launch
import retrofit2.Response

class AddressViewModel(private val repository: Repository) : ViewModel() {
    val country = MutableLiveData<Response<CountriesResponse>>()
    val state = MutableLiveData<Response<StateResponse>>()
    val city = MutableLiveData<Response<CityResponse>>()
    val addAddress = MutableLiveData<Response<AddressResponse>>()
    val defaultAddress = MutableLiveData<Response<AddressResponse>>()
    val deleteAddress = MutableLiveData<Response<DeleteReminderResponse>>()
    val getAddress = MutableLiveData<Response<GetAddressResponse>>()

    fun getCountry() {
        viewModelScope.launch {
            country.value = repository.getCountry()
        }
    }

    fun getState(country: Int) {
        viewModelScope.launch {
            state.value = repository.getState(country)
        }
    }

    fun getCity(state: Int) {
        viewModelScope.launch {
            city.value = repository.getCity(state)
        }
    }

    fun addAddress(body: AddressBody) {
        viewModelScope.launch {
            addAddress.value = repository.addAddress(body)
        }
    }

    fun deleteAddress(id: Int) {
        viewModelScope.launch {
            deleteAddress.value = repository.deleteAddress(id)
        }
    }

    fun defaultAddress(id: Int, checked: Int) {
        viewModelScope.launch {
            defaultAddress.value = repository.makeDefaultAddress(id, checked)
        }
    }

    fun getAddressByUser() {
        viewModelScope.launch {
            getAddress.value = repository.getAddressByUser()
        }
    }
}