package com.fitness.fitnessCru.quickbox.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun addFragment(
    fragmentManager: FragmentManager,
    containerId: Int,
    fragment: Fragment,
    tag: String
) {
    fragmentManager.beginTransaction().replace(containerId, fragment, tag).commitAllowingStateLoss()
}

