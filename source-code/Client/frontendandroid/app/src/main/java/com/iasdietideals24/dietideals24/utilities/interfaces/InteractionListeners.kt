package com.iasdietideals24.dietideals24.utilities.interfaces

import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass

interface OnFragmentChangeActivity {
    fun <Activity : AppCompatActivity> onFragmentChangeActivity(activity: Class<Activity>)
}

interface OnFragmentOpenUrl {
    fun onFragmentOpenUrl(externalUrl: String)
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

fun interface OnFragmentEditButton {
    fun onFragmentEditButton(id: Long, sender: KClass<*>)
}

interface OnGoToDetails {
    fun onGoToDetails(id: Long, sender: KClass<*>)
}

interface OnGoToProfile {
    fun onGoToProfile(id: Long, sender: KClass<*>)
}