package com.fitness.fitnessCru.response

data class CoachListingResponse(
    val error_code: Int,
    val message: String,
    val data: List<Data>
) {
    data class Data(
        val id: Int,

        val coach_name: String,

        val coach_email: String,

        val coach_specialization_ids: String,

        val coach_slab_type: String? = null,

        val image: String? = null,

        val image_url: String? = null,

        val coach_category_id: Long,
        val coach_slab_type_details: CoachSlabTypeDetails? = null,
        val coach_specialization: List<CoachSpecialization>? = null
    ) {
        data class CoachSlabTypeDetails(
            val id: Int,
            val coach_type: String? = null
        )

        data class CoachSpecialization(
            val id: Int,
            val name: String,
            val image: String,
            val image_url: String
        )
    }
}
