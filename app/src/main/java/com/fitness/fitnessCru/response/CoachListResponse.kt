package com.fitness.fitnessCru.response

data class CoachListResponse(
    val error_code: Int,

    val message: String,
    val data: List<Datum>? = null
) {
    data class Datum(
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

        val coach_biodata: String? = null,

        val language_ids: String,

        val coach_specialization_ids: String,

        val coach_category_id: Int,

        val gym_association_ids: String,

        val coach_slab_type: String? = null,

        val adhar_card_no: String,

        val adhar_card_front_image: String,

        val aadhar_front_image_url: Any? = null,

        val adhar_card_back_image: String,

        val aadhar_back_image_url: Any? = null,

        val passport_no: Any? = null,

        val gender: String,
        val status: String,

        val working_day: Any? = null,

        val working_hours: Any? = null,

        val no_of_slots: String,

        val available_slots: String? = null,

        val discount_percent: String,

        val coach_instagram_link: String? = null,

        val commission_percentage_per_client: String? = null,

        val fixed_remuneration: String? = null,

        val monthly_pay_out: String? = null,

        val mondy_to_frdy_strt_time: String? = null,

        val mondy_to_frdy_end_time: String? = null,

        val strdy_end_time: String? = null,

        val strdy_strt_time: String? = null,
        val total_clients: Int? = null,
        val experience: Int? = null,
        val location: String? = null

    )
}
