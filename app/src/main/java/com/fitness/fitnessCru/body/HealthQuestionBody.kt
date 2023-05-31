package com.fitness.fitnessCru.body

import com.fitness.fitnessCru.response.ImageUploadResponse

data class HealthQuestionBody(
//    val user_id: Int,
    val question_data: ArrayList<Question>,
    val Image: ArrayList<ImageUploadResponse.Data>? = null
) {
    data class Question(val id: Int, val name: String, val answer: ArrayList<String>)
}
