package com.fitness.fitnessCru.response

data class UserDetailsResponse(
    val error_code: Int,
    val message: String,
    val data: Data,
    val user_data: Data
)