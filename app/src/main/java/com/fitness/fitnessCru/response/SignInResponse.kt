package com.fitness.fitnessCru.response

data class SignInResponse(val error_code: Int, val message: String, val data: DataSignin)

data class DataSignin(
    val id: Int,
    val name: String,
    val email: String,
    val usertype: Int,
    val email_verified_at: Any,
    val created_at: String,
    val updated_at: String,
    val role: Int,
    val status: Int,
    val pending: Int,
    val device_token: Any,
    val provider_id: Any,
    val provider_name: Any,
    val phone_number: String,
    val otp: Int,
    val image: String,
    val image_url: Any,
    val dob: Any,
    val gender: String,
    val access_token: String

)
