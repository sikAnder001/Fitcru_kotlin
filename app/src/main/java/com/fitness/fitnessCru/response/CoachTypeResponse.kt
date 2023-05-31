package com.fitness.fitnessCru.response

data class CoachTypeResponse(
    val error_code: Int,
    val message: String,
    val data: List<Data>
) {
    data class Data(
        val id: Int,
        val name: String,
        val image: String? = null,
        val image_url: String? = null,
        val what_you_get: String? = null
    )
}
