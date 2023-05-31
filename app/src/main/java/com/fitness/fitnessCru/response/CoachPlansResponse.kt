package com.fitness.fitnessCru.response

import com.google.gson.annotations.SerializedName

data class CoachPlansResponse(
    val error_code: Int,
    val message: String,
    val data: Data? = null
) {
    data class Data(
        @SerializedName("1month_price") val onemonth_price: MonthPrice? = null,
        @SerializedName("3month_price") val threemonth_price: MonthPrice? = null,
        @SerializedName("6month_price") val sixmonth_price: MonthPrice? = null
    ) {
        data class MonthPrice(
            val actual_price: Int,
            val discount_price: Int,
            val offer_price: Int
        )
    }
}
