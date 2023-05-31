package com.fitness.fitnessCru.body

data class ConsumedMealBody(
    val nutrition_id: Int,
    val food_id: Int,
    val time: String,
    val date: String
)

data class ConsumedAllMealBody(
    val foods: List<Foods>
) {
    data class Foods(
        val nutrition_id: Int,
        val food_id: Int,
        val time: String,
        val date: String
    )
}