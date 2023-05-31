package com.fitness.fitnessCru.response

import java.io.Serializable

data class SignUpWithEmailResponse(
    val error_code: Int,
    val message: String,
    val data: SignUpWithEmailData
)

data class SignUpWithEmailData(
    val id: Int,
    val email: String,
    val email_verification_code: String,
    val access_token: String
) : Serializable
