package com.fitness.fitnessCru.response

data class ForgotPasswordResponse(
    val data: ForgetPassData,
    val error_code: Int,
    val message: String
) {
    data class ForgetPassData(
        val id: Int,
        val email: String,
        val email_verification_code: String,
        val access_token: String
    )
}