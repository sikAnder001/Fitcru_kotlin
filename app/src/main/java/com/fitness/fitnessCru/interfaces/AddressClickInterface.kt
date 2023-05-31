package com.fitness.fitnessCru.interfaces

import android.view.View
import com.fitness.fitnessCru.response.GetAddressResponse

interface AddressClickInterface {

    fun onEditClicked(view: View, data: GetAddressResponse.Data, position: Int)

}