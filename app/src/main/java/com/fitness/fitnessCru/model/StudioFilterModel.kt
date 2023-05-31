package com.fitness.fitnessCru.model

data class StudioFilterModel(
    val filterTitle: String,
    val filterChildItems: ArrayList<StudioFilterChildItem>
) {

    data class StudioFilterChildItem(
        val filterTypes: String
    )
}

