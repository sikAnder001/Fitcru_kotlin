package com.fitness.fitnessCru.body

data class BookAppointmentBody(
    val coach_time_slot_id: Int,
    val appointment_date: String,
    val appointment_day_name: String,
    val coach_id: Int
)
