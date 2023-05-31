package com.fitness.fitnessCru.model

import java.io.Serializable

data class CoachBundle(
    val price: String,
    val key: String,
    val tabId: Int,
    val tabName: String,
    val slabId: Int,
    val slabName: String,
    val duration: String,
    val feeId: Int
) :
    Serializable
