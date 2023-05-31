package com.fitness.fitnessCru.body

import java.io.Serializable

data class UpdateQuestionBody(

    var dob: String,
    var gender: String,
    var height: String,
    var weight: String,
    var what_bring_select_id: Int,
    var diet_type_select_id: Int,
    var fitness_level_select_id: Int,
    var how_active_select_id: Int,
    var workout_vibe_select_id: Int,

    ) : Serializable

data class UpdateQuestionBody2(
    var height: String,
    var weight: String,
    var target_weight: String? = null,
    val steps: String,
    val water_intake: String,
    var what_bring_select_id: Int,
    var diet_type_select_id: Int,
    var fitness_level_select_id: Int,
    var how_active_select_id: Int,
    val workout_vibe_select_id: Int,
    val sleep_time: String,
    val wakeup_time: String
) : Serializable
