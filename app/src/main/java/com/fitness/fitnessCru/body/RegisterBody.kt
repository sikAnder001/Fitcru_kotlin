package com.fitness.fitnessCru.body

data class RegisterBody(
    val name: String,
    val phone_number: String,
    val email: String,
    val password: String,
    val confirm_password: String,

    )