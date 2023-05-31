package com.fitness.fitnessCru.response

data class ConsumedMealResponse(
    val data: Data,
    val error_code: Int,
    val message: String
) {
    data class Data(
        val date: String,
        val food_id: Int,
        val id: Int,
        val time: String,
        val user_id: Int
    )
}