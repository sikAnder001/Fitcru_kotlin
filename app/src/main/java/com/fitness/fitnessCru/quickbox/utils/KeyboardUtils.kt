package com.fitness.fitnessCru.quickbox.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.fitness.fitnessCru.utility.Session


fun hideKeyboard(view: View) {
    val imm =
        Session.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}