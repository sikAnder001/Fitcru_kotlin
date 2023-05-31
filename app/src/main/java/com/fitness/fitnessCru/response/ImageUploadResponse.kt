package com.fitness.fitnessCru.response

data class ImageUploadResponse(
    val error_code: Int,
    val message: String,
    val data: ArrayList<Data>
) {
    data class Data(var image_url: String)
}
