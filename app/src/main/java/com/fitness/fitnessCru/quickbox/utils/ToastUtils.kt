package com.fitness.fitnessCru.quickbox.utils

import android.widget.Toast
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import com.fitness.fitnessCru.utility.Session


@IntDef(Toast.LENGTH_LONG, Toast.LENGTH_SHORT)
private annotation class ToastLength

fun shortToast(@StringRes text: Int) {
    shortToast(Session.getInstance().getString(text))
}

fun shortToast(text: String) {
    show(text, Toast.LENGTH_SHORT)
}

fun longToast(@StringRes text: Int) {
    longToast(Session.getInstance().getString(text))
}

fun longToast(text: String) {
    show(text, Toast.LENGTH_LONG)
}

private fun makeToast(text: String, @ToastLength length: Int): Toast {
    return Toast.makeText(Session.getInstance(), text, length)
}

private fun show(text: String, @ToastLength length: Int) {
    makeToast(text, length).show()
}