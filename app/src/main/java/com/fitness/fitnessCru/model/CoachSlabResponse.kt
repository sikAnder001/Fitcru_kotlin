package com.fitness.fitnessCru.model


data class CoachSlabResponse(
    val error_code: Int,
    val message: String,
    val data: List<Data>
) {
    data class Data(
        val id: Int,
        val coach_type: String,
    )
}
