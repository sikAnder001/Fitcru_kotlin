package com.fitness.fitnessCru.body

import com.fitness.fitnessCru.response.FoodListResposne

data class MealBody(
    var meal_type_id: Int,
    var date: String,
    var time: String,
    var food_data: ArrayList<FoodData>
) {
    data class FoodData(
        var food_id: Int,
        var total_calorie: Double,
        var unit: String,
        var food_unit_id: Int,
        var serving_size_id: Int,
        var quantity: Double,
        var meal_name: String,
        var meal_type_id: Int,
        var id: Int? = null,
        var oneSize: Double? = null,
        val serving_sizes: ServingSize? = null,
        val units_list: List<FoodListResposne.Data.UnitsList>? = null
    ) {

        data class ServingSize(
            val id: Int,
            val food_id: Int,
            val food_unit_id: Int,
            val serving_size: String,
            val unit: String,
            val calorie: String,

            val carbs: String,
            val fat: String,
            val fiber: String,
            val protein: String
        )
    }
}