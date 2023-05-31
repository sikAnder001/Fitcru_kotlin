package com.fitness.fitnessCru.response

data class FoodListResposne(
    val `data`: ArrayList<Data>,
    val error_code: Int,
    val message: String
) {
    class Data(
        val calories: String,
        val carbs: String,
        val created_at: String,
        val deleted: Int,
        val fat: String,
        val fiber: String,
        val id: Int,
        val image: Any,
        val image_url: Any,
        val img: Any,
        val name: String,
        val proteins: String,
        val serving_size: Int,
        val tags: String,
        val unit: String,
        val updated_at: String,
        val servin_size: List<ServingSize>,
        val units_list: List<UnitsList>
    ) {

        data class UnitsList(
            val food_unit_id: Int,
            val weight: Int,
            val measure: String,
            val food_unit_name: String
        ) {
            override fun toString(): String {
                return food_unit_name
            }
        }

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