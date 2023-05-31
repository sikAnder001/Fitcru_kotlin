package com.fitness.fitnessCru.response

data class PaymentResponse(
    val id: String? = null,
    val entity: String,
    val amount: Int,
    val amount_paid: Int,
    val amount_due: Int,
    val currency: String,
    val receipt: String,
    val offer_id: String,
    val status: String,
    val attempts: Int,
    val notes: List<String>,

    )