package com.fitness.fitnessCru.body

data class ReminderBody(
    val reminder_id: Int,
    val reminder_title: String,
    val reminder_description: String,
    val reminder_time: String,
    val reminder_before_time: String,
    val repeat: String
) {
    constructor(
        reminder_title: String,
        reminder_description: String, reminder_time: String,
        reminder_before_time: String,
        repeat: String
    ) : this(
        0,
        reminder_title,
        reminder_description,
        reminder_time,
        reminder_before_time,
        repeat
    ) {
    }
}