package com.fitness.fitnessCru.response

data class MySummaryResponse(
    val error_code: Int,
    val message: String,
    val weight: List<Weight>? = null,
    val water: List<Water>? = null,
    val sleep: List<Sleep>? = null,
    val step: List<Step>? = null,
    val fat: List<Fat>? = null
) {
    data class Weight(
        val id: Int,
        val user_id: Int,
        val weight: String? = null,
        val date: String
    )

    data class Water(
        val id: Int,
        val user_id: Int,
        val water_intake: String? = null,
        val date: String
    )

    data class Sleep(
        val id: Int,
        val user_id: Int,
        val total_sleep_time: String? = null,
        val date: String
    )

    data class Step(
        val id: Int,
        val user_id: Int,
        val steps: String? = null,
        val date: String
    )

    data class Fat(
        val id: Int,
        val user_id: Int,
        val fat: String? = null,
        val date: String
    )

}
