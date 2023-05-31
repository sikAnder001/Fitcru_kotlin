package com.fitness.fitnessCru.response

data class WhatBringsYouResponse(
    val error_code: Int,
    val message: String,
    val data: List<WorkoutVibeData>
) {

    data class WorkoutVibeData(
        val id: Int,
        val title: String? = null,
        val description: String,
        val image: String,
        val image_url: String,
        val name: String? = null,

        val status: Int,
        val created_at: String,
        val updated_at: String
    ) {
        override fun toString(): String {
            return title ?: ""
        }
    }
}
