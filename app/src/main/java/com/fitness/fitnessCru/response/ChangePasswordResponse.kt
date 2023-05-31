package com.fitness.fitnessCru.response

import java.io.Serializable

data class ChangePasswordResponse(
    val error_code: Int,
    val message: String
) : Serializable
