package com.fitness.fitnessCru.response

class GradeAndSpecialResponse(
    val error_code: Int,
    val message: String,
    val data: Data
) {
    data class Data(
        val specialization: List<Specialization>,
        val grade: List<Grade>
    ) {
        data class Grade(
            val id: Int,
            val coach_type: String
        )

        data class Specialization(
            val id: Int,
            val name: String,
            val image: String,
            val image_url: String
        )

    }
}
