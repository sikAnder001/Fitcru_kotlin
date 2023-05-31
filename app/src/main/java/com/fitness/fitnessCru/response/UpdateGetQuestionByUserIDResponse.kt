package com.fitness.fitnessCru.response

data class UpdateGetQuestionByUserIDResponse(
    val error_code: Int,
    val message: String,
    val data: List<DataHealth>? = null,
    val water: List<WaterHealth>? = null,
    val sleep: List<SleepHealth>? = null,
    val step: List<StepHealth>? = null
) {
    data class DataHealth(
        val id: Int,
        val user_id: Int,
        val height: String? = null,
        val weight: String? = null,
        val what_bring_select_id: Int,
        val diet_type_select_id: Int,
        val fitness_level_select_id: Int,
        val how_active_select_id: Int,
        val workout_vibe_select_id: Int,
        val target_weight: String? = null,

        )

    data class WaterHealth(
        val id: Int,
        val user_id: Int,
        val water_intake: String? = null,
        val date: String,
    )

    data class SleepHealth(
        val id: Int,
        val user_id: Int,
        val sleep_time: String? = null,
        val wakeup_time: String? = null,
        val total_sleep_time: String,
        val date: String
    )

    data class StepHealth(
        val id: Int,
        val user_id: Int,
        val steps: String? = null,
        val date: String
    )
}
