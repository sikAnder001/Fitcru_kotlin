package com.fitness.fitnessCru.body

import java.io.Serializable

data class CoachData(
    val id: Int,
    val coach_name: String? = null,
    val image_url: String? = null,
    val location: String? = null,
    val years_experience: Int? = null,
    val clients_experience: Int? = null,
    val rating: Int? = null
) : Serializable