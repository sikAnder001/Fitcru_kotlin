package com.fitness.fitnessCru.body

data class AddressBody(
    val address_id: Int = 0,
    val country_id: Int,
    val state_id: Int,
    val city_id: Int,
    val address: String,
    val locality: String,
    val landmark: String,
    val pincode: String,
    val phone_number: String? = null
)