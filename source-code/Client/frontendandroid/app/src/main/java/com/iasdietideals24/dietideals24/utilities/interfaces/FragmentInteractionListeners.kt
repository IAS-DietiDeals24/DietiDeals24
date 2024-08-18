package com.iasdietideals24.dietideals24.utilities.interfaces

import androidx.appcompat.app.AppCompatActivity

interface OnFragmentChangeActivity {
    fun <Activity : AppCompatActivity> onFragmentChangeActivity(activity: Class<Activity>)
}

fun interface OnFragmentBackButton {
    fun onFragmentBackButton()
}

fun interface OnFragmentHideBackButton {
    fun onFragmentHideBackButton()
}

fun interface OnFragmentShowBackButton {
    fun onFragmentShowBackButton()

}