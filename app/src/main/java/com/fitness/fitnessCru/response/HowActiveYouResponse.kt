package com.fitness.fitnessCru.response

data class HowActiveYouResponse(
    val error_code: Int,
    val message: String,
    val data: List<HowActiveYouData>
) {
    data class HowActiveYouData(
        val id: Int,
        val created_at: Any,
        val updated_at: Any,
        val name: String,
        val activeness: String,
        val detail: String,
        val image: String,
        val image_url: String,
        val status: Int

    ) {
        override fun toString(): String {
            return name
        }
    }
}