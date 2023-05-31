package com.fitness.fitnessCru.model

import com.fitness.fitnessCru.response.CoachPlansResponse

data class SubPlanParentModel(
    val id: Int,
    var intro: String? = null,
    var price: String,
    var month: String,
    var btnText: String,
    var childModel: List<SubPlanChildModel>,
    var color: Int,
    var actualOffer: CoachPlansResponse.Data.MonthPrice? = null
)