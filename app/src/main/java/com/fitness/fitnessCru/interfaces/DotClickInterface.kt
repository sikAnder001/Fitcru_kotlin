package com.fitness.fitnessCru.interfaces

import android.view.View
import com.fitness.fitnessCru.response.GetAllReminderResponse

interface DotClickInterface {

    fun onDotClick(view: View, data: GetAllReminderResponse.Data, position: Int)

}