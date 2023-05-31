package com.fitness.fitnessCru.response

data class GetPlanDetailResponse(
    val error_code: Int,
    val message: String,
    val data: Data
) {
    data class Data(

        val user_selected_feestructure: UserSelectedFeestructure,

        val coach_details: CoachDetails,

        val total_price: String,

        val discount_percent: Double,

        val gst: Int,

        val total_paybale: Double
    ) {
        data class UserSelectedFeestructure(
            val id: Int,
            val fee_structure_id: Int,
            val coach_category_id: Int,
            val coach_slab_type: String,
            val fee_structure_key: String,
            val user_select_price: String,
            val user_id: Int,
            val coach_id: Int
        )

        data class CoachDetails(
            val id: Int,

            val coach_name: String,

            val coach_email: String,

            val email_verification_code: Any? = null,

            val email_verified_at: Any? = null,

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

            val coach_category_id: Int,

            val gym_association_ids: String,

            val coach_slab_type: String,

            val adhar_card_no: String,

            val adhar_card_front_image: String,

            val adhar_card_back_image: String,

            val passport_no: Any? = null,

            val gender: String,
            val status: String,

            val working_day: Any? = null,

            val working_hours: Any? = null,

            val no_of_slots: String,

            val available_slots: String,

            val discount_percent: String,

            val coach_instagram_link: Any? = null,

            val commission_percentage_per_client: Any? = null,

            val fixed_remuneration: Any? = null,

            val coach_specialization: List<CoachSpecializationData>,

            val coach_location: CoachLocationData,
        ) {

            data class CoachSpecializationData(
                val id: Int,
                val name: String? = null,
                val image: String? = null,
                val image_url: String? = null
            ) {
                override fun toString(): String {
                    return name ?: ""
                }
            }

            data class CoachLocationData(
                val id: Int,
                val coach_id: Int? = null,
                val location_name: String? = null,
            )

        }
    }
}