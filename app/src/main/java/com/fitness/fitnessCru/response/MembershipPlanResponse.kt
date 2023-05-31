package com.fitness.fitnessCru.response

import java.io.Serializable

data class MembershipPlanResponse(
    val error_code: Int,
    val message: String,
    val Programs: ArrayList<Data>,
    val Coaches: ArrayList<MCoaches>,
    val Plans: ArrayList<MPlans>? = null

) {
    data class Data(
        val program_id: Int,
        val name: String? = null,
        val image: String,
        val image_url: String,
        val start_date: String,
        val end_date: String,
        val days_left: Int
    )

    data class MCoaches(
        val coach_id: Int,
        val name: String? = null,
        val plan_name: String? = null,
        val image: String,
        val image_url: String,
        val start_date: String? = null,
        val end_date: String,
        val days_left: Int
    )

    data class MPlans(
        val plan_id: Int,
        val name: String? = null,
        val image: String,
        val image_url: String,
        val start_date: String? = null,
        val end_date: String,
        val days_left: Int
    ) : Serializable

}