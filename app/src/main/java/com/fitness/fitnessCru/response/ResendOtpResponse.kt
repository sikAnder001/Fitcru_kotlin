package com.fitness.fitnessCru.response

data class ResendOtpResponse(val error_code: Int, val message: String, val data: ResendData)

class ResendData(
    val number: String,
    val otp: String
)