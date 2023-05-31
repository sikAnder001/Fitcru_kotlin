package com.fitness.fitnessCru.response

data class GetCoachDetailsResponse(
    val error_code: Int,
    val message: String,
    val data: Data
) {
    data class Data(
        val id: Int,
        val user_id: Int,
        val coach_name: String,
        val coach_email: String,
        val email_verification_code: String? = null,
        val phone_number: String,
        val otp: String? = null,
        val password: String,
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
        val adhar_card_front_image: String? = null,
        val aadhar_front_image_url: String? = null,
        val adhar_card_back_image: String,
        val aadhar_back_image_url: String? = null,
        val passport_no: String? = null,
        val gender: String,
        val status: String,
        val working_day: Any? = null,
        val working_hours: Any? = null,
        val no_of_slots: String,
        val available_slots: String,
//        val discount_percent: String,
        val coach_instagram_link: Any? = null,
        val commission_percentage_per_client: Any? = null,
        val fixed_remuneration: Any? = null,
        val coach_specialization: ArrayList<CoachSpecializations>,
        val coach_slab_type_details: coachSlabTypeDetails,
        val coach_certificate: ArrayList<CoachCertificate>,
        val coach_reviews: ArrayList<CoachReviews>,
        val coach_category: coachCategory? = null,
        val coach_location: CoachLocation? = null

    )

    data class coachSlabTypeDetails(
        val id: Int,
        val coach_type: String
    )

    data class coachCategory(
        val id: Int,
        val name: String
    )

    data class CoachSpecializations(
        val id: Int,
        val name: String,
        val image_url: String,
        val image: String
    )

    data class CoachCertificate(
        val id: Int? = null,
        val coach_id: Int? = null,
        val name: String? = null,
        val image: String? = null,
        val description: String? = null,
        val comment: String? = null,
        val status: String? = null,
        val image_url: String? = null,
        val certificate_name: String? = null,
        val certi_reject_reason: String? = null

    )

    data class CoachReviews(
        val id: Int? = null
    )

    data class CoachLocation(
        val id: Int? = null,
        val coach_id: Int? = null,
        val location_name: String? = null
    )
}