package com.fitness.fitnessCru.body

import java.io.Serializable

data class SignInBody(
    val name: String,
    val email: String,
    val phone_number: String,
    val password: String,
    val confirm_password: String
) : Serializable
