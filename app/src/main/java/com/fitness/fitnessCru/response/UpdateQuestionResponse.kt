package com.fitness.fitnessCru.response

data class UpdateQuestionResponse(
    val data: List<Data>,
    val error_code: Int,
    val message: String
) {
    data class Data(
        val created_at: Any,
        val description: String,
        val id: Int,
        val image: Any,
        val image_url: Any,
        val status: Any,
        val title: String,
        val updated_at: Any
    )
}