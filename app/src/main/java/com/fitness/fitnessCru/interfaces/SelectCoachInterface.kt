package com.fitness.fitnessCru.interfaces

import android.os.Bundle
import android.view.View

interface SelectCoachInterface {

    fun onBookClicked(view: View, position: Int, nav: Int, bundle: Bundle)
}