package com.fitness.fitnessCru.response

data class WorkoutDetailResponse(
    val error_code: Int,
    val message: String,
    val data: Data
) {
    data class Data(
        val id: Int,
        val banner: String,
        val name: String,
        val firstline: String,
        val secondline: String,
        val thirdline: String,
        val description: String
    )
}
