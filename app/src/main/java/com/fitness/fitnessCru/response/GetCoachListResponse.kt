package com.fitness.fitnessCru.response

import java.io.Serializable

data class GetCoachListResponse(
    val data: ArrayList<Data>,
    val error_code: Int,
    val message: String
) : Serializable {

    data class Data(
        val adhar_card_back_image: String,
        val adhar_card_front_image: String,
        val adhar_card_no: String,
        val available_slots: String,
        val coach_biodata: String,
        val coach_category: CoachCategory,
        val coach_category_id: Int,
        val coach_email: String,
        val coach_instagram_link: String,
        val coach_location: CoachLocation,
        val coach_name: String,
        val coach_qualifications: String,
        val coach_slab_type: String,
        //val coach_slab_type_details: CoachSlabTypeDetails,
        val coach_specialization: ArrayList<CoachSpecialization>,
        val coach_specialization_ids: String,
        val created_at: String,
        val dob: String,
        val doj: String,
        val gender: String,
        val gym_association_ids: String,
        val id: Int,
        val image: String,
        val image_url: String,
        val languagess: String,
        val no_of_slots: String,
        val password: String,
        val phone_number: String,
        val status: String,
    ) : Serializable {

        data class CoachCategory(
            val id: Int,
            val name: String,
            val updated_at: String
        ) : Serializable

        data class CoachLocation(
            val coach_id: Int,
            val id: Int,
            val location_name: String
        ) : Serializable

        data class CoachSpecialization(val name: String, val id: Int) : Serializable
    }
}