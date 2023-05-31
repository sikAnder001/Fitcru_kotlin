package com.fitness.fitnessCru.body

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class UpdateUserDetailsBody(
    val first_name: RequestBody,
    val phone_number: RequestBody,
    val email: RequestBody,
    val gender: RequestBody,
    val dob: RequestBody,
    val image: MultipartBody.Part?
)
