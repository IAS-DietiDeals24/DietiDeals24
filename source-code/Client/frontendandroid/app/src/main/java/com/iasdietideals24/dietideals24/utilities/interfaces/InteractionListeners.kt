package com.iasdietideals24.dietideals24.utilities.interfaces

import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass

interface OnFragmentChangeActivity {
    fun <Activity : AppCompatActivity> onFragmentChangeActivity(activity: Class<Activity>)
}

fun interface OnFragmentOpenUrl {
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
    fun onFragmentEditButton(sender: KClass<*>)
}

fun interface OnFragmentGoToHome {
    fun onFragmentGoToHome()
}

fun interface OnFragmentGoToAuction {
    fun onFragmentGoToAuction(id: Long, sender: KClass<*>)
}

fun interface OnFragmentNextStep {
    fun onFragmentNextStep(sender: KClass<*>)
}

fun interface OnFragmentSkipStep {
    fun onFragmentSkipStep(sender: KClass<*>)
}

fun interface OnGoToDetails {
    fun onGoToDetails(id: Long, sender: KClass<*>)
}

fun interface OnGoToProfile {
    fun onGoToProfile(id: Long, sender: KClass<*>)
}