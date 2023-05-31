package com.fitness.fitnessCru.response

data class ChatListResponse(
    val error_code: Int,
    val message: String,
    val data: ArrayList<Data>
) {
    data class Data(
        val id: Int,
        val user_id: Int,
        val last_message: String,
        val last_message_time: String,
        val last_message_updated_time: String,
        val coach_id: Int,
        val coach_name: String,
        val image: String,
        val image_url: String
    )
}
