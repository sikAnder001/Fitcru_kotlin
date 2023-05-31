package com.fitness.fitnessCru.response

data class RecipeDetailResponse(
    val data: Data,
    val error_code: Int,
    val message: String
) {

    data class Data(
        val cooking_time: Int,
        val description: String,
        val food: Food,
        val food_id: Int,
        val how_to_make: String? = null,
        val image_url: String,
        val video_url: String,
        val id: Int,
        val preparation_time: String? = null,
        val serving: String,
        val tag: List<String>? = null
    )

    data class Food(
        val alergies: String,
        val calories: String,
        val carbs: String,
        val cooking_today: String,
        val created_at: String,
        val deleted: Int,
        val diet_preference_id: Int,
        val fat: String,
        val fiber: String,
        val foodtype_id: Int,
        val health_issues: String,
        val id: Int,
        val image: Any,
        val image_url: String,
        val img: Any,
        val iron: String,
        val name: String,
        val phosp: String,
        val potassium: String,
        val proteins: String,
        val reflink: Any,
        val rich: String,
        val rich_value: String,
        val serving_size: Int,
        val size_per_unit: Any,
        val sulphur: String,
        val tags: String,
        val unit: String,
        val updated_at: String,
        val vitamina: String,
        val vitaminb: String,
        val weight_gram: Int
    )
}