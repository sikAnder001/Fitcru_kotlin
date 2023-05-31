package com.fitness.fitnessCru.model

import java.io.Serializable


data class SNCProgramModel(
    val error_code: Int,
    val message: String,
    val data: Data
) : Serializable {
    data class Data(
        val name: String,
        val phases: Int,
        val weeks: Int,

        val image_url: String? = null,

        val description: String? = null,
        val totalsession: Int,

        val duration_weeks: Int,

        val sessionlist: List<Sessionlist>,
        val fitness_level: FitnessLevel
    ) : Serializable {

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

        data class Sessionlist(
            val week: Int,
            val session: List<Session>? = null
        ) : Serializable {
            data class Session(
                val id: Int? = null,

                val img: String? = null,

                val video: String? = null,

                val video_url: String? = null,

                val day: Int? = null,

                val banner: String? = null,
                val session_name: String? = null,

                val duration: String? = null,

                val add_to_id: Int? = null,

                val session_type: String? = null
            ) : Serializable
        }
    }
}