package com.fitness.fitnessCru.interfaces

interface CoachPlanClickInterface {

    fun onCoachClick(
        key: String,
        price: String,
        coach_category_id: Int,
        feeId: Int,
        slab_type: String,
        duration: String
    )
}