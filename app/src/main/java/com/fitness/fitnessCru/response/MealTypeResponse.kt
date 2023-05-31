package com.fitness.fitnessCru.response

data class MealTypeResponse(
    val data: List<Data>,
    val error_code: Int,
    val message: String
) {
    data class Data(
        val id: Int,
        val mealtype: String,
        val updated_at: String,
        val created_at: String
    ) {
        override fun toString(): String {
            return mealtype
        }

    }
}