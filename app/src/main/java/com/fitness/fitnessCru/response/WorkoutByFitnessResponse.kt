package com.fitness.fitnessCru.response

data class WorkoutByFitnessResponse(
    val error_code: Int,
    val message: String,
    val data: List<WorkoutByFitnessData>
) {
    data class WorkoutByFitnessData(
        val id: Int,
        val banner: String? = null,
        val session_name: String? = null,
        val description: String? = null,
        val types: String? = null,
        val studio: String? = null,
        val benefits: String? = null,
        val workouts: Any? = null,
        val equipment: String? = null,
        val duration: String? = null,
        val intensity: String? = null,
        val level: String? = null,
        val tags: String? = null,
        // val exercises_data:List<ExercisesData>? = null,
        val focus: String? = null,
        val musclegroup: String? = null,
        val service: String? = null,
        val cals: String? = null,
        val img: String? = null,
        val video: String? = null,
        val status: Int? = null,
        val updated_at: String? = null,
        val created_at: String? = null,
        val fitness_level: List<FitnessLevel>
    ) {
        data class FitnessLevel(
            val id: Int,
            val name: String,
            val level: Double,
            val detail: String,
            val image: String,

            val image_url: String,

            val status: String
        )
    }
}
