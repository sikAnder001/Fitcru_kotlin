package com.fitness.fitnessCru.response

data class StudioDetailsResponse(
    val error_code: Int,
    val message: String,
    val data: StudioData? = null
) {
    data class StudioData(
        val id: Int,
        val banner: String,
        val title: String,
        val status: Int,
        val description: String,
        val image_url: String,
        val workouts: List<StudioWorkouts>? = null
    ) {
        data class StudioWorkouts(
            val id: Int,
            val banner: String,
            val session_name: String,
            val description: String,
            val types: Types,
            val studio: String,
            val video: String? = null,
            val views: String? = null

        ) {
            data class Types(
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
}