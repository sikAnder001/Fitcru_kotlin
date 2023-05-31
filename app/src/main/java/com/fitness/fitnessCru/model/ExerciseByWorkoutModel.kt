package com.fitness.fitnessCru.model

data class ExerciseByWorkoutModel(
    val data: Data,
    val error_code: Int,
    val message: String
) {

    data class Data(
        val id: Int,
        val banner: String,

        val session_name: String,

        val description: String,
        val types: String,
        val studio: String,
        val benefits: String,
        val workouts: Any? = null,
        val equipment: String,
        val duration: String,
        val intensity: String,
        val level: String,
        val tags: String,


        var exercises_data: ArrayList<ExerciseData>,

        val focus: String,
        val musclegroup: String,
        val service: String,
        val cals: Any? = null,
        val img: Any? = null,
        val video: Any? = null,
        val status: Int

    ) {

        data class ExerciseData(
            val ex_type: String,
            val details: Details
        ) {
            data class Details(
                val sets: String? = null,
                val sets_type: String? = null,
                val description: String? = null,
                var exercise: ArrayList<Exercise>? = null
            ) {
                data class Exercise(
                    val exercise_id: String? = null,

                    val exercizetype_id: String? = null,

                    val banner: String? = null,
                    val video: String? = null,
                    val title: String? = null,
                    val target: String? = null,

                    val target_text: String? = null,

                    val rest: String? = null,
                    val sets: String? = null,

                    val main_rest: Any? = null,

                    val target_type: String? = null,

                    val description: String? = null,

                    val weights: String? = null,

                    val fullWeight: String? = null,
                    val fullTime: String? = null,
                    val fullReps: String? = null,
                    val ex_type: String? = null,
                    val description2: String? = null,
                    val ex_type2: String? = null,
                    val sets_type: String? = null,
                    val sets2: String? = null,


                    )
            }
        }
    }
}
