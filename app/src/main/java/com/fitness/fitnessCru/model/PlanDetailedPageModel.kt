package com.fitness.fitnessCru.model

data class PlanDetailedPageModel(
    val coachImg: Int,
    val coachName: String,
    val coachAddress: String,
    val rating: String,
    val selected: String,
    val slotAvailable: String,
    val planDetailedChildItemModel: ArrayList<PlanDetailedChildItemModel>
) {
    data class PlanDetailedChildItemModel(val text: String)
}