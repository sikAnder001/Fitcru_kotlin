package com.fitness.fitnessCru.response

data class SubscriptionPlanResponse(
    val data: Data,
    val error_code: Int,
    val message: String
) {

    data class Data(
        val id: Int,


        val coach_category_id: Int,


        val coach_slab_type: String,


        val coach_fee_structure: CoachFeeStructure,

        val current_price: CurrentPrice,


        val coach_slab_detail: CoachSlabDetail,


        val coach_category: CoachCategory

    )

    data class CurrentPrice(
        val current_price_one_months: String,

        val current_price_three_months: String,

        val current_price_six_months: String
    )

    data class CoachCategory(
        val id: Long,
        val name: String,

        )

    data class CoachFeeStructure(
        val actual_price_one_months: String,

        val actual_price_three_months: String,

        val actual_price_six_months: String
    )

    data class CoachSlabDetail(
        val id: Int? = null,

        val coach_type: String? = null
    )

}
