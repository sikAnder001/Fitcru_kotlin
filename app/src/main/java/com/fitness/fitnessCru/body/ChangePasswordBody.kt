package com.fitness.fitnessCru.body

import java.io.Serializable

data class ChangePasswordBody(
    val old_password: String,
    val password: String,
    val confirm_password: String,
    val quickBloxId: String,


    ) : Serializable
