package com.fitness.fitnessCru.response

data class TrendingOfferResponse(
    val data: Data,
    val error_code: Int,
    val message: String
) {

    data class Data(
        val client_id: Int,
        val created_at: String,
        val description: String,
        val fitness_level: FitnessLevel,
        val fitness_level_id: Int,
        val gender: String,
        val goal_id: Int,
        val goal_name: GoalName,
        val how_to_make: String,
        val id: Int,
        val current_price: String? = null,
        val image: String,
        val image_url: String,
        val main_muscle_id: Int,
        val name: String,
        val phases: Int,
        val price: String,
        val saveTo: String,
        val status: Int,
        val studio_id: String,
        val style_of_workout_id: Any,
        val type: String,
        val updated_at: Any,
        val weeks: Int,
        val what_you_get: String,
        val coaches: List<Coaches>
    )

    data class Coaches(
        val id: Int,

        val coach_name: String,

        val coach_email: String,

        val email_verification_code: Any? = null,

        val phone_number: String,

        val otp: Any? = null,

        val image_url: String,

        val dob: String,
        val doj: String,

        val coach_specialization_ids: String,

        val coach_category_id: Long,

        val gender: String,
        val status: String,

        val no_of_slots: String,

        val available_slots: String? = null,

        )

    data class FitnessLevel(
        val created_at: Any,
        val detail: String,
        val id: Int,
        val image: String,
        val image_url: String,
        val level: Double,
        val name: String,
        val status: String,
        val updated_at: Any
    )

    data class GoalName(
        val created_at: Any,
        val description: String,
        val id: Int,
        val image: String,
        val image_url: String,
        val status: String,
        val title: String,
        val updated_at: Any
    )
}