package com.fitness.fitnessCru.response

data class GetCoachSpecializationsResponse(
    val error_code: Int,
    val message: String,
    val data: List<SpecializationsData>
) {
    data class SpecializationsData(
        val id: Int,
        val name: String,
        val image: String,
        val created_at: String,
        val updated_at: String,
        val image_url: String
    )
}
