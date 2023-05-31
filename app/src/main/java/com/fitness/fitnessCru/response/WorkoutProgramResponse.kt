package com.fitness.fitnessCru.response

data class WorkoutProgramResponse(
    val `data`: Data,
    val error_code: Int,
    val message: String
) {
    data class Data(
        val benifits: ArrayList<Benefit>,
        val goals_with_program: ArrayList<GoalsWithProgram>,
        val happy_customers: ArrayList<HappyCustomer>,
        val programs_sliders: ArrayList<ProgramsSlider>,
        val trending_offers: ArrayList<TrendingOffer>
    ) {

        data class Benefit(
            val id: Int,
            val image: String,
            val image_url: String,
            val title: String,
            val description: String
        )

        data class GoalsWithProgram(
            val created_at: Any,
            val description: String,
            val id: Int,
            val image: String,
            val image_url: String,
            val programs: ArrayList<Program>,
            val status: Any,
            val title: String,
            val updated_at: Any
        ) {
            data class Program(
                val client_id: Int,
                val created_at: String,
                val description: String,
                val fitness_level_id: Int,
                val gender: String,
                val image_url: String,
                val goal_id: Int,
                val id: Int,
                val name: String,
                val phases: Int,
                val price: String,
                val saveTo: String,
                val status: Int,
                val studio_id: Any,
                val type: String,
                val updated_at: String,
                val weeks: Int
            )
        }

        data class HappyCustomer(
            val client_name: String,
            val client_short_description: String,
            val created_at: Any,
            val description: String,
            val id: Int,
            val image: String,
            val image_url: String,
            val rating_count: String,
            val updated_at: Any
        )

        data class ProgramsSlider(
            val created_at: Any,
            val id: Int,
            val image: String,
            val image_url: String,
            val title: String,
            val updated_at: Any
        )

        data class TrendingOffer(
            val client_id: Int,
            val created_at: String,
            val description: String,
            val fitness_level: FitnessLevelData? = null,
            val fitness_level_id: Any,
            val gender: String,
            val goal_id: Int,
            val goal_name: GoalName,
            val id: Int,
            val name: String,
            val phases: Int,
            val price: String,
            val saveTo: String,
            val status: Int,
            val studio_id: Any,
            val type: String,
            val updated_at: String,
            val image_url: String,
            val weeks: Int
        ) {

            data class GoalName(
                val created_at: Any,
                val description: String,
                val id: Int,
                val image: String,
                val image_url: String,
                val status: Any,
                val title: String?,
                val updated_at: Any
            )

            data class FitnessLevelData(
                val id: Int,
                val name: String,
                val level: Double,
                val detail: String,
                val image: String,
                val image_url: String,
                val status: String,

                )
        }
    }
}