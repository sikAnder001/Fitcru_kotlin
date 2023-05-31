package com.fitness.fitnessCru.response

data class MobileLoginResponse(val error_code: Int, val message: String, val data: Data)

data class Data(
    val access_token: String,
    val created_at: String,
    val device_token: Any,
    val dob: String? = null,
    val email: String,
    val email_verified_at: Any,
    val gender: String,
    val id: Int,
    val image: String,
    val image_url: String,
    val name: String,
    val otp: String,
    val pending: Int,
    val phone_number: String,
    val provider_id: Any,
    val provider_name: Any,
    val role: Int,
    val status: Int,
    val updated_at: String,
    val usertype: Int,
    val questionnaire: Questionnaire,
    val bmi_calculation: BmiCalculation,

    val social_id: String,

    val login_by: String,

    val ispaid: Int,
    val email_verification_code: String? = null,

    val workout_vibes: List<WorkoutVibeHealth>,
    val planDetail: List<PlanDetail>? = null,
    var quick_blox_id: String? = null,


    ) {

    data class PlanDetail(
        val coach_id: Int? = null,
        val subscription_plan_id: Int,
        val start_date: String,
        val end_date: String
    )

    data class WorkoutVibeHealth(
        val id: Int,
        val title: String,
        val description: String,
        val image_url: String,
    ) {
        override fun toString(): String {
            return title;
        }
    }

    data class BmiCalculation(
        val bmi: Double,
        val bmr: Double,
        val tdee: Double
    )

    data class Questionnaire(
        val daily_activity: List<DailyActivity>,
        val fitness_level: List<FitnessLevel>,
        val food_preference: List<FoodPreference>,
        val your_goals: List<YourGoal>,
        val how_active_select_id: Int,
        val fitness_level_select_id: Int,
        val diet_type_select_id: Int,
        val what_bring_select_id: Int,
        val workout_vibe_select_id: Int,
        val height: String,
        val weight: String,
        val target_weight: String? = null,
        val user_id: Int,
    ) {

        data class DailyActivity(
            val activeness: Double,
            val created_at: Any,
            val detail: String,
            val id: Int,
            val image: String,
            val image_url: String,
            val name: String,
            val status: String,
            val updated_at: Any
        ) {
            override fun toString(): String {
                return name;
            }
        }

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
        ) {
            override fun toString(): String {
                return name;
            }
        }

        data class FoodPreference(
            val created_at: String,
            val description: Any,
            val id: Int,
            val image: String,
            val image_url: String,
            val name: String,
            val status: String,
            val updated_at: String
        ) {
            override fun toString(): String {
                return name;
            }
        }

        data class YourGoal(
            val created_at: Any,
            val description: String,
            val id: Int,
            val image: String,
            val image_url: String,
            val name: String,
            val status: String,
            val title: String,
            val updated_at: Any,
            val value: String
        ) {
            override fun toString(): String {
                return name;
            }
        }
    }
}