package com.fitness.fitnessCru.body

data class GetCoachListBody(
    val coach_category_id: Int,
    val coach_slab_type: Int? = null,
    val coach_specialization_id: Int
)

data class GetCoachListBody2(
    val coach_category_id: Int,
    val coach_slab_type: Int
)
