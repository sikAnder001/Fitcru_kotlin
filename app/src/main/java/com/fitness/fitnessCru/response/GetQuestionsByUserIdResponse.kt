package com.fitness.fitnessCru.response

data class GetQuestionsByUserIdResponse(
    val error_code: Int,
    val message: String,
    val data: GetQuestionsByUserIdData
) {
    data class GetQuestionsByUserIdData(
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
        val mySummary: GetMySummaryData? = null,
        val food_preference_detail: FoodPreferenceDetails? = null,
        val fitness_level_detail: FitnessLevelDetails? = null,
        val daily_activity_detail: DailyActivityDetails? = null,
        val target_step: String? = null,
        val target_water: String? = null,
        val your_goal_detail: YourGoalDetails? = null,
        val workout_vibe_detail: WorkoutVibeDetails? = null,


        ) {
        data class GetMySummaryData(

            val userSteps: UserSteps? = null,

            val userWater: UserWater? = null,

            val userSleep: UserSleep? = null,
        ) {

            data class UserSteps(
                val id: Int,
                val user_id: Int,
                val steps: String? = null,
                val date: String
            )

            data class UserWater(
                val id: Int,
                val user_id: Int,
                val water_intake: String? = null,
                val date: String
            )

            data class UserSleep(
                val id: Int,
                val user_id: Int,
                val sleep_time: String? = null,
                val wakeup_time: String? = null,
                val total_sleep_time: String,
                val date: String
            )

        }

        data class FoodPreferenceDetails(
            val id: Int,
            val name: String,
            val description: String? = null,
            val image: String,
            val image_url: String,
            val status: String

        )

        data class FitnessLevelDetails(
            val id: Int,
            val name: String,
            val level: Double,
            val detail: String,
            val image: String,
            val image_url: String,
            val status: String
        )

        data class DailyActivityDetails(
            val id: Int,
            val name: String,
            val activeness: Double,
            val detail: String,
            val image: String,
            val image_url: String,
            val status: String
        )

        data class YourGoalDetails(
            val id: Int,
            val name: String,
            val value: String,
            val title: String,
            val description: String,
            val image: String,
            val image_url: String,
            val status: String
        )

        data class WorkoutVibeDetails(
            val id: Int,
            val title: String,
            val description: String,
            val image: String,
            val image_url: String,
            val status: String
        )

    }

}