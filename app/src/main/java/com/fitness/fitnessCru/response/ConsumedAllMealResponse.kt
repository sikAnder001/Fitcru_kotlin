package com.fitness.fitnessCru.response

data class ConsumedAllMealResponse(
    val error_code: Int,
    val message: String,
    val data: List<Datum>
) {
    data class Datum(
        val id: Int,

        val food_id: Int,

        val user_id: Int,

        val time: String,
        val date: String,

        val nutrition_id: Int,

        val meal_type_id: Int
    )

}
