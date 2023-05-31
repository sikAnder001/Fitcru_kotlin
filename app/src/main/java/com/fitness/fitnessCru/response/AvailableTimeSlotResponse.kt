package com.fitness.fitnessCru.response

import com.google.gson.annotations.SerializedName

data class AvailableTimeSlotResponse(
    val `data`: Data,
    val error_code: Int,
    val message: String
) {

    data class Data(
        val available_time_slots: ArrayList<AvailableTimeSlot>,
        val coach_details: CoachDetails
    ) {

        data class AvailableTimeSlot(
            val coachTimeSlotId: Int,
            val slotTime: String? = null,
            val book_date: String? = null,
            val duration: String? = null,
            @SerializedName("break")
            val break1: String? = null,
        )

        data class CoachDetails(
            val adhar_card_back_image: String,
            val adhar_card_front_image: String,
            val adhar_card_no: String,
            val available_slots: String,
            val coach_biodata: String,
            val coach_category_id: Int,
            val coach_email: String,
            val coach_instagram_link: Any,
            val coach_name: String,
            val coach_qualifications: String,
            val coach_slab_type: String,
            val coach_specialization_ids: String,
            val commission_percentage_per_client: Any,
            val discount_percent: String,
            val dob: String,
            val doj: String,
            val email_verification_code: Any,
            val email_verified_at: Any,
            val fixed_remuneration: Any,
            val gender: String,
            val gym_association_ids: String,
            val id: Int,
            val image: String,
            val image_url: String,
            val languagess: String,
            val no_of_slots: String,
            val otp: Any,
            val passport_no: Any,
            val phone_number: String,
            val status: String,
            val working_day: Any,
            val working_hours: Any,
            val location: String
        )
    }
}