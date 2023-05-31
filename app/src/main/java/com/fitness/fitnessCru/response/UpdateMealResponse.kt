package com.fitness.fitnessCru.response

data class UpdateMealResponse(
    val `data`: ArrayList<Data>,
    val error_code: Int,
    val message: String
) {
    data class Data(
        val created_at: String,
        val food: Food,
        val food_id: Int,
        val id: Int,
        val meal_in_weight: String,
        val meal_name: String,
        val meal_type: MealType,
        val meal_type_id: String,
        val time: String,
        val total_calorie: String,
        val updated_at: String,
        val user_details: UserDetails,
        val user_id: Int
    ) {
        data class Food(
            val alergies: String,
            val calories: String,
            val carbs: String,
            val created_at: String,
            val deleted: Int,
            val diet_preference_id: Int,
            val fat: String,
            val fiber: String,
            val foodtype_id: Int,
            val health_issues: String,
            val id: Int,
            val image: Any,
            val image_url: Any,
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

        data class UserDetails(
            val created_at: String,
            val device_token: Any,
            val dob: Any,
            val email: Any,
            val email_verification_code: Any,
            val email_verified_at: Any,
            val gender: String,
            val id: Int,
            val image: Any,
            val image_url: Any,
            val name: Any,
            val otp: String,
            val pending: Int,
            val phone_number: String,
            val provider_id: Any,
            val provider_name: Any,
            val role: Int,
            val status: Int,
            val updated_at: String,
            val usertype: Int
        )

        data class MealType(
            val created_at: String,
            val id: Int,
            val mealtype: String,
            val updated_at: String
        )

    }
}