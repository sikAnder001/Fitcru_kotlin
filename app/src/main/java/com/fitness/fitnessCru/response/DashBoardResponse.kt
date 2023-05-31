package com.fitness.fitnessCru.response

import java.io.Serializable

data class DashBoardResponse(
    val data: Data,
    val error_code: Int,
    val message: String
) {

    data class Data(
        val cooking_today: ArrayList<CookingToday>,
        val progress: Progress,
        val todays_task: ArrayList<TodayTask>,
        val transformation: ArrayList<Transformation>,
        val trending_seasons: ArrayList<TrendingSession>,
        val workout: ArrayList<Workout>? = null,
        val your_coach: ArrayList<YourCoach>?,
        val habit: ArrayList<Habit>?
    ) {

        data class Habit(
            val id: Int,

            val habit_category_id: Int,

            val name: String,
            val content: String,
            val video: String,

            val video_url: Any? = null,

            val banner: String,
            val status: Long,
            val time: Any? = null,
            val cals: Any? = null,
            val equipment: Any? = null,

            )


        data class YourCoach(
            val id: Int,

            val coach_name: String,

            val coach_email: String,

            val email_verification_code: String? = null,

            val email_verified_at: String? = null,

            val phone_number: String,

            val otp: Any? = null,
            val image: String,

            val image_url: String,

            val dob: String,
            val doj: String,

            val coach_qualifications: String,

            val coach_biodata: String,

            val language_ids: String,

            val coach_specialization_ids: String,

            val coach_category_id: Long,

            val gym_association_ids: String,

            val coach_slab_type: String,

            val adhar_card_no: String,

            val adhar_card_front_image: String? = null,

            val aadhar_front_image_url: Any? = null,

            val adhar_card_back_image: String? = null,

            val aadhar_back_image_url: Any? = null,


            val gender: String,
            val status: String,

            val no_of_slots: String,

            val available_slots: String,
        )

        data class Workout(
            val banner: String,
            val benefits: String,
            val cals: String,
            val coach_name: String? = null,
            val description: String,
            val duration: String? = null,
            val equipments: String?,
            val focus: String,
            val id: Int,
            val img: String,
            val intensity: String,
            val level: String,
            val musclegroup: String,
            val service: String,
            val session_name: String,
            val status: Int,
            val studio: String,
            val tags: String,
            val types: String,
            val video: String,
            val video_url: String? = null,
            val workouts: String,
            val session_type: String,
            val cardio_target: String? = null,
            val cardio_val: String? = null
        )

        data class CookingToday(
            val alergies: Any,
            val calories: String,
            val carbs: String,
            val cooking_today: String,
            val created_at: String,
            val deleted: Int,
            val diet_preference_id: Int,
            val fat: String,
            val fiber: String,
            val foodtype_id: Int,
            val health_issues: Any,
            val id: Int,
            val image: Any,
            val image_url: String,
            val img: Any,
            val iron: String,
            val name: String,
            val phosp: String,
            val potassium: String,
            val proteins: String,
            val reflink: String,
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
            val weight_gram: Int,
            val total_time: String? = null
        )

        data class Progress(
            val diet: Diet,
            val steps: Steps,
            val water: Water,
            val workout: Workout
        ) {
            data class Diet(
                val diet_val: String,
                val kcal_burnt: String,
                val unit: String
            )

            data class Steps(
                val step_count_val: String,
                val step_target: String
            )

            data class Water(
                val water_taget: String,
                val water_used_val: String,
                val unit: String
            )

            data class Workout(
                val workout_kcal_target: String,
                val workout_kcal_burnt: String,
                val unit: String
            )
        }

        data class TodayTask(
            val created_at: Any,
            val date: String,
            val food_id: Int,
            val id: Int,
            val meal_name: String,
            val meal_type_id: Int,
            val meal_type_name: MealTypeName,
            val quantity: String,
            val time: String,
            val total_calorie: String,
            val unit: String,
            val updated_at: Any,
            val is_complete: Int, //TODO this key not in api
            val user_id: Int
        ) {

            data class MealTypeName(
                val id: Int,
                val mealtype: String,
                val foods: Food
            ) : Serializable {

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
                    val image: String,
                    val image_url: String,
                    val img: Any,
                    val iron: String,
                    val name: String,
                    val phosp: String,
                    val potassium: String,
                    val proteins: String,
                    val reflink: String,
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
                    val weight_gram: Int,
                    val servin_size: List<ServinSize>

                ) : Serializable {
                    data class ServinSize(
                        val id: Int,

                        val food_id: Long,

                        val serving_size: String,

                        val unit: String,
                        val calorie: String
                    )
                }
            }
        }

        data class Transformation(
            val after_image: String,
            val after_image_url: String,
            val age: String,
            val before_image: String,
            val before_image_url: String,
            val created_at: String,
            val id: Int,
            val month: String,
            val name: String,
            val updated_at: String,
            val weight: String,
            val weight_type: String
        )

        data class TrendingSession(
            val client_id: Int,
            val created_at: String,
            val description: String,
            val fitness_level: FitnessLevel? = null,
            val fitness_level_id: Int,
            val gender: String,
            val goal_id: Int,
            val how_to_make: String,
            val id: Int,
            val image: String,
            val image_url: String,
            val main_muscle_id: Int,
            val name: String,
            val phases: Int,
            val price: String,
            val saveTo: String,
            val status: Int,
            val studio_id: String,
            val style_of_workout_id: Any,
            val total_kcal: Int,
            val total_session: Int,
            val total_time: String,
            val total_view: String,
            val type: String,
            val updated_at: Any,
            val weeks: Int,
            val what_you_get: String
        ) {

            data class FitnessLevel(
                val created_at: Any,
                val detail: String,
                val id: Int,
                val image: String,
                val image_url: String,
                val level: Double,
                val name: String,
                val status: String,
                val updated_at: Any
            )
        }

    }
}