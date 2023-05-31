package com.fitness.fitnessCru.model

data class Message(
    val attachment_display_name: String = "",
    val attachment_path: String = "",
    val attachment_server_name: String = "",
    val attachment_type: String = "",
    val chat_message: String = "",
    val chat_sender_Id: String = "",
    val chat_time: String = "",
    var read_time: String = ""
)
