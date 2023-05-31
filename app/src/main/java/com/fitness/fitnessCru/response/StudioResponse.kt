package com.fitness.fitnessCru.response

data class StudioResponse(val error_code: Int, val message: String, val data: Data) {

    data class Data(
        val studios: List<Studio>,

        val trending_session: List<TrendingSeasons>,

        val new_collection: List<NewCollection>,

        val workout_by_intensity: List<WorkoutByIntensity>,

        val popular_collection: List<PopularCollection>,

        val train_with_best_coach: List<TrainWithBestCoach>
    )

    data class Studio(
        val id: Int,

        val created_at: String? = null,

        val updated_at: String? = null,

        val banner: String? = null,
        val title: String,
        val status: Int? = null,
        val description: String,
        val image: String? = null,

        val image_url: String? = null
    )

    data class TrendingSeasons(

        val total_session: Int,

        val total_time: String,

        val total_kcal: Int,

        val id: Int,

        val style_of_workout_id: Any? = null,

        val client_id: Int,

        val name: String,
        val type: String,
        val phases: Int,
        val weeks: Int,
        val saveTo: String,
        val description: String,

        val created_at: String,

        val updated_at: Any? = null,

        val status: Int,

        val goal_id: Int,

        val fitness_level_id: Int,

        val price: String,

        val studio_id: String,

        val gender: String,

        val how_to_make: String,

        val what_you_get: String,

        val main_muscle_id: Int,

        val image: String,

        val image_url: String,

        val view: String,

        val fitness_level: FitnessLevel? = null

    ) {

        data class FitnessLevel(
            val id: Int,

            val created_at: Any? = null,

            val updated_at: Any? = null,

            val name: String,
            val level: Double,
            val detail: String,
            val image: String,

            val image_url: String,

            val status: String
        )


    }

    data class NewCollection(
        val id: Int,

        val created_at: String,

        val updated_at: String,

        val banner: String,
        val title: String,
        val status: Long,
        val description: String,

        val image_url: String
    )

    data class PopularCollection(
        val id: Int,

        val created_at: Any? = null,

        val updated_at: Any? = null,

        val name: String,
        val image: Any? = null,
        val detail: String,

        val image_url: Any? = null
    )

    data class WorkoutByIntensity(
        val id: Int,

        val client_id: Int,

        val name: String,
        val image_url: String,
        val total_session: Int,
        val fitness_level_id: Int,

        val type: String,
        val phases: Int,
        val weeks: Int,
        val saveTo: String,
        val description: String,

        val created_at: String,

        val updated_at: String,

        val status: Int,

        val goal_id: Int,

        val price: Any? = null,

        val studio_id: Any? = null,

        val gender: String,

        val goal_name: Studio,

        val fitness_level: FitnessLevel? = null

    )

    data class FitnessLevel(
        val id: Int,

        val created_at: Any? = null,

        val updated_at: Any? = null,

        val name: String,
        val level: Double,
        val detail: String,
        val image: String,

        val image_url: String,

        val status: String
    )

    data class TrainWithBestCoach(
        val id: Int,
        val coach_name: String,
        val coach_email: String,
        val email_verification_code: String,
        val email_verified_at: String,
        val phone_number: String,
        val otp: String,
        val image: String,
        val image_url: String,
        val dob: String,
        val doj: String,
        val coach_qualifications: String,
        val coach_biodata: String,
        val language_ids: String,
        val coach_specialization_ids: String,
        val coach_category_id: String,
        val gym_association_ids: String,
        val coach_slab_type: String,
        val adhar_card_no: String,
        val adhar_card_front_image: String,
        val aadhar_front_image_url: String,
        val adhar_card_back_image: String,
        val aadhar_back_image_url: String,
        val passport_no: String,
        val gender: String,
        val status: String,
        val working_day: String? = null,
        val working_hours: String? = null,
        val no_of_slots: String,
        val available_slots: String,
        val coach_instagram_link: String? = null,
        val commission_percentage_per_client: String? = null,
        val fixed_remuneration: Any? = null,
        val monthly_pay_out: Any? = null,
        val mondy_to_frdy_strt_time: Any? = null,
        val mondy_to_frdy_end_time: Any? = null,
        val strdy_end_time: Any? = null,
        val strdy_strt_time: Any? = null,
        val specializations: List<Specializations>? = null
    ) {
        data class Specializations(
            val id: Int? = null,
            val name: String,
            val image: String? = null,
            val image_url: String? = null
        ) {
            override fun toString(): String {
                return name
            }
        }
    }

}

