package com.fitness.fitnessCru.interfaces

interface FeedbackInterface {

    fun onFeedbackClick(
        pos: Int,
        feedId: String?,
        title: String,
        description: String,
        weight: String,
        time: String? = null,
        raps: String? = null
    )
}