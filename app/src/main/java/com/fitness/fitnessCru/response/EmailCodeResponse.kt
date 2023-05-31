package com.fitness.fitnessCru.response

data class EmailCodeResponse(val error_code: Int, val message: String, val data: OTPdata)

data class OTPdata(
    val id: Int,
    val email: String,
    val eamil_verification_code: String,
    val access_token: String
)