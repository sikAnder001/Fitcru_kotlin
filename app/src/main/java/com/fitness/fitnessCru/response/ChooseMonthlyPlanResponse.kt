package com.fitness.fitnessCru.response

data class ChooseMonthlyPlanResponse(
    val error_code: Int,
    val message: String,
    val data: Data
) {
    data class Data(
        val id: Int,
        val fee_structure_id: Int,
        val coach_category_id: Int,
        val coach_slab_type: String,
        val user_select_price: String,
        val user_id: Int,
        val coach_id: Any? = null
    )
}
