package com.fitness.fitnessCru.response

data class DietTypeResponse(
    val data: List<Data>,
    val error_code: Int,
    val message: String
) {

    data class Data(
        val created_at: String,
        val description: String,
        val id: Int,
        val image: String,
        val image_url: String,
        val name: String,
        val status: String,
        val title: String,
        val updated_at: String,
        val value: String
    ) {
        override fun toString(): String {
            return name
        }
    }
}