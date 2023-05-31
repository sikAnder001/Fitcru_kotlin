package com.fitness.fitnessCru.response


data class CoachCategoryResponse(
    val error_code: Int,
    val message: String,
    val data: List<Data>
) {
    data class Data(
        val id: Int,
        val name: String,
    )
}