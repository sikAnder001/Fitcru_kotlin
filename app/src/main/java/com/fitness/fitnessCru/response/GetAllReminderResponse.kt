package com.fitness.fitnessCru.response

data class GetAllReminderResponse(
    val data: List<Data>,
    val error_code: Int,
    val message: String
) {
    data class Data(
        val created_at: String,
        val id: Int,
        val reminder_before_time: String,
        val reminder_description: String,
        val reminder_time: String,
        val reminder_title: String,
        val repeat: String,
        val updated_at: String,
        val user_id: Int
    )
}