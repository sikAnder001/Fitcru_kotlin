package com.fitness.fitnessCru.response

data class HabitsResponse(
    val error_code: Int,
    val message: String,
    val data: List<HabitsListData>
) {
    data class HabitsListData(
        val id: Int,
        val habit_category_id: Int,
        val name: String? = null,
        val content: String? = null,
        val video: String? = null,
        val banner: String,
        val status: Int? = null,
        val time: String? = null,
        val cals: String? = null,
        val equipment: String? = null,
        val habit_category: HabitCategory
    ) {
        data class HabitCategory(
            val id: Int,
            val name: String? = null
        )
    }
}
