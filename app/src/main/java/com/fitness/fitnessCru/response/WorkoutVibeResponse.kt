package com.fitness.fitnessCru.response

import java.io.Serializable

data class WorkoutVibeResponse(
    val error_code: Int,
    val message: String,
    val data: List<WhatBringsYouData>
) {

    data class WhatBringsYouData(
        val id: Int,
        val title: String? = null,
        val description: String? = null,
        val image: String? = null,
        val image_url: String? = null,
        val name: String? = null
    ) : Serializable {
        override fun toString(): String {
            return title.toString()
        }
    }
}