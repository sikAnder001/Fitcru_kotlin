package com.fitness.fitnessCru.response

data class GetAddressResponse(
    val data: List<Data>? = null,
    val error_code: Int,
    val message: String
) {
    data class Data(
        val id: Int? = null,
        val user_id: String,
        val country_id: Int,
        val state_id: Int,
        val city_id: Int,
        val country_name: CountryName,
        val city_name: CountryName,
        val state_name: CountryName,
        val address: String,
        val locality: String,
        val landmark: String,
        val pincode: String,
        val is_default: String,
        val user_detils: UserDetails,
        val updated_at: String,
        val created_at: String,
        val phone_number: String? = null,
    ) {
        data class CountryName(val name: String? = null)

        data class UserDetails(
            /* val id: Int,
             val name: String,
             val email: String,
             val usertype: Int,
             val email_verified_at: String,
             val role: Int,
             val status: Int,
             val pending: Int,*/
            val phone_number: String? = null,
            /* val image: String,
             val image_url: String,
             val dob: String,
             val gender: String,
             val email_verification_code: String,*/
        )
    }
}

