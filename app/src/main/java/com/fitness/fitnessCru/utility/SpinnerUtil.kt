package com.fitness.fitnessCru.utility

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.fitness.fitnessCru.R


object SpinnerUtil {
    // Set item in spinner according to the position
    fun setItemAtPosition(spinner: Spinner, array: ArrayList<String>, value: String) {
        for (i in array.indices) {
            if (array[i].equals(value, ignoreCase = true)) {
                spinner.setSelection(i)
                // spinner.dropDownVerticalOffset=200
                break
            } else
                spinner.setSelection(array.size - 1)
        }
    }


    //set the data to the spinner
    fun setSpinner(spinner: Spinner, array: ArrayList<String>, context: Context) {

        //spinner.dropDownVerticalOffset = 100
        spinner.setPopupBackgroundResource(R.drawable.edit_profile_edit_text_bg)
        // Creating adapter for spinner

        val dataAdapter = ArrayAdapter(context, R.layout.layout_spinner, array)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        // attaching data adapter to spinner
        spinner.adapter = dataAdapter

    }


}