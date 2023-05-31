package com.fitness.fitnessCru.response

data class CongratulationResponse(
    val data: Data,
    val error_code: Int,
    val message: String
) {
    data class Data(
        val bmiCalculation: BmiCalculation,
        val bmi_value: BmiValue,
        val normal: Int,
        val over_weight: Int,
        val under_weight: Int
    ) {
        data class BmiCalculation(
            val bmr: String? = null,
            val tdee: Double
        )

        data class BmiValue(
            val bmi: Double
        )
    }
}