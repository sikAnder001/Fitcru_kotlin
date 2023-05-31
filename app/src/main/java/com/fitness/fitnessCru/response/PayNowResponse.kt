package com.fitness.fitnessCru.response

import java.io.Serializable

data class PayNowResponse(
    val error_code: Long,

    val message: String,
    val data: Data
) {
    data class Data(
        val id: String,

        val amount: Int,

        val amount_capturable: Long,


        val amount_received: Long,

        val application: Any? = null,

        val application_fee_amount: Any? = null,

        val automatic_payment_methods: Any? = null,

        val cancellation_reason: Any? = null,

        val capture_method: String,


        val client_secret: String,

        val confirmation_method: String,

        val created: Int,
        val currency: String,
        val customer: Any? = null,
        val description: String,
        val invoice: Any? = null,

        val last_payment_error: Any? = null,

        val livemode: Boolean,
        val metadata: List<Any?>,

        val next_action: Any? = null,

        val on_behalf_of: Any? = null,

        val payment_method: Any? = null,

        val payment_method_types: List<String>,

        val processing: Any? = null,

        val receipt_email: Any? = null,

        val review: Any? = null,

        val setup_future_usage: Any? = null,

        val shipping: Any? = null,
        val source: Any? = null,

        val statement_descriptor: Any? = null,

        val statement_descriptor_suffix: Any? = null,

        val status: String,

        val transfer_data: Any? = null,

        val transfer_group: Any? = null,


        ) : Serializable
}