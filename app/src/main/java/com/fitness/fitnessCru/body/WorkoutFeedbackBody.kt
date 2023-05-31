package com.fitness.fitnessCru.body

data class WorkoutFeedbackBody(
    val workout_id: Int,
    val rate_session: String? = null,
    val feedback_message: String? = null,
    val exercise_data: List<ExerciseDataFeedback>/*? = null*/
) {
    data class ExerciseDataFeedback(
        val exercise_id: Int? = null,
        val weight: String? = null,
        val reps: String? = null,
        val time: String? = null
    )
}