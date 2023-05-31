package com.fitness.fitnessCru.response

data class FitnessLevelResponse(
    val data: List<Data>,
    val error_code: Int,
    val message: String
) {
    data class Data(
        val created_at: String,
        val detail: String,
        val id: Int,
        val image: String,
        val image_url: String,
        val level: Double,
        val name: String,
        val status: String,
        val updated_at: String
    ) {
        override fun toString(): String {
            return name
        }
    }
}