package com.fitness.fitnessCru.response

data class UpcomingAppointmentResponse(
    val errorCode: Int,
    val message: String,
    val ClientAppointment: List<ClientAppointments>
) {
    data class ClientAppointments(
        val id: Int,
        val appointment_no: String,

        val user_id: Int,

        val coach_time_slot_id: Int,
        val appointment_date: String,

        val appointment_day_name: String,

        val note: String? = null,

        val is_book: String,

        val time_slot: TimeSlot
    ) {
        data class TimeSlot(
            val id: Int,

            val availability_id: Int,

            val slot_start_time: String? = null,

            val slot_end_time: String,

            val coach_id: Int,

            val is_available: String,

            val slot_strdy_strt_time: String? = null,

            val slot_strdy_end_time: Any? = null,

            val coach: Coach
        ) {
            data class Coach(
                val id: Int,

                val coach_name: String,

                val image: String,

                val image_url: String
            )
        }
    }
}
