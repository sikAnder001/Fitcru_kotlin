package com.fitness.fitnessCru.model

data class CGetCoachModel(
    val duration: String,
    val price: String? = null,
    val coach_category_id: Int,
    val feeId: Int,
    val slab_type: String? = null,
    val key: String,
    val currentPrice: String? = null
)
