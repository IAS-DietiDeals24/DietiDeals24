package com.iasdietideals24.dietideals24.utilities.interfaces

import androidx.appcompat.app.AppCompatActivity

fun interface OnFragmentChangeDate {
    fun onFragmentChangeDate(string: String)
}

interface OnFragmentChangeActivity {
    fun <Activity : AppCompatActivity> onFragmentChangeActivity(activity: Class<Activity>)
}

interface OnFragmentBackButton {
    fun onFragmentBackButton()
}
