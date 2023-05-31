package com.fitness.fitnessCru.response

data class AddressResponse(
    val data: Data,
    val error_code: Int,
    val message: String
) {

    data class Data(
        val address: String,
        val city_id: Int,
        val country_id: Int,
        val id: Int,
        val landmark: String,
        val locality: String,
        val pincode: String,
        val state_id: Int,
        val user_id: Int
    )
}