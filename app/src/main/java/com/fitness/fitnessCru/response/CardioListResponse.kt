package com.fitness.fitnessCru.response

data class CardioListResponse(
    val error_code: Int,
    val message: String,
    val data: List<CardioListData>
) {
    data class CardioListData(
        val id: Int,
        val cardioexercise_id: Int,
        val name: String,
        val time: String,
        val image: String,
        val video: String,
        val notes: String,
        val equipments: String,
        val cals: String,
        val created_at: String,
        val updated_at: String,
        val cardio_excercise: CardioExercise
    ) {
        data class CardioExercise(
            val id: Int,
            val name: String,
            val created_at: String,
            val updated_at: String
        )
    }
}


