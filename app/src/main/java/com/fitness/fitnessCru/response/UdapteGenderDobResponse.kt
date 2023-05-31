package com.fitness.fitnessCru.response

data class UpdateGenderDobResponse(
    val `data`: Data,
    val error_code: Int,
    val message: String
) {
    data class Data(
        val access_token: String,
        val dob: String,
        val email: String,
        val email_verification_code: String,
        val gender: String,
        val id: Int
    )
}