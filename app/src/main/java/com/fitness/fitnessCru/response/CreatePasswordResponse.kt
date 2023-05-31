package com.fitness.fitnessCru.response

data class CreatePasswordResponse(val error_code: Int, val message: String, val data: Data) {
    data class Data(
        val id: Int,
        val email: String,
        val email_verification_code: String,
        val access_token: String


    )
}