package com.fitness.fitnessCru.response

data class PlanBreakUpResponse(
    val `data`: Data,
    val error_code: Int,
    val message: String
) {
    data class Data(
        val actual_price: Int,
        val discount_price: Int,
        val gst: Int,
        val here_what_you_get: List<HereWhatYouGet>? = null,
        val offer_percentage: Int,
        val offer_price: Int,
        val plan_months_count: Int,
        val total_payble_amount: Int
    ) {
        data class HereWhatYouGet(
            val id: Int,
            val image: String,
            val image_url: String,
            val name: String,
            val what_you_get: String
        )
    }
}