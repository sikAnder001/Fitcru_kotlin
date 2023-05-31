package com.fitness.fitnessCru.model

data class YogaSearchModel(
    val yogaSearchTitle: String,
    val yogaSearchChildItem: ArrayList<YogaSearchChildItem>
)

data class YogaSearchChildItem(
    val thumbnailImage: Int,
    val views: String,
    val yogaTitle: String,
    val yogaChoose: String,
    val yogaDuration: String,
    val yogaCategory: String
)
