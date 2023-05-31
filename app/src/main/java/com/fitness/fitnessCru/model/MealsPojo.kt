package com.fitness.fitnessCru.model

data class MealsPojo(
    val errorCode: Int,

    val message: String,
    val data: Data
) {
    data class Data(

        val total_calorie: String,

        val consume_calorie: String,

        val total_carbs: String,

        val consume_carbs: String,

        val total_protein: String,

        val consume_protein: String,

        val total_fat: String,

        val consume_fat: String,

        val user_selected_meals: List<UserSelectedMeal>
    )

    data class UserSelectedMeal(
        val id: Int,

        val food_id: Int,

        val time: String,

        val meal_name: String,

        val meal_type_id: Int,

        val quantity: String? = null,

        val food_unit_id: Int?,

        val unit: String,

        val meal_type: MealType,

        val total_calorie: String,

        val date: String,

        val image_url: String? = null,

        val meal_type_name: MealTypeName,

        val is_complete: Int,

        val units_list: List<UnitsList>
    )

    data class UnitsList(

        val food_unit_id: Int,

        val weight: Int,

        val food_unit_name: String
    )

    data class MealTypeName(
        val id: Int,
        val mealtype: String,
        val foods: Food? = null
    ) {
        data class Food(
            val id: Int,

            val foodtype_id: Int,

            val name: String,
            val image: String? = null,
            val alergies: String? = null,

            val health_issues: String? = null,

            val calories: String,
            val proteins: String,
            val fat: String,
            val carbs: String,
            val sulphur: String,
            val phosp: String,
            val potassium: String,
            val fiber: String,
            val vitamina: String,
            val iron: String,
            val vitaminb: String,
            val reflink: String? = null,
            val tags: String,

            val serving_size: Int,

            val unit: String,
            val deleted: Long,

            val weight_gram: Int,

            val img: Any? = null,

            val size_per_unit: Any? = null,

            val rich: String,

            val rich_value: String,

            val image_url: String,

            val cooking_today: String,

            val servin_size: ServinSize
        ) {
            data class ServinSize(
                val serving_size: String,
                val unit: String,
            )
        }
    }

    data class MealType(
        val id: Long,
        val mealtype: String,

        )

}

