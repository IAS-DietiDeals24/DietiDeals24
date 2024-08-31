package com.iasdietideals24.dietideals24.utilities.interfaces

import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass

interface OnChangeActivity {
    fun <Activity : AppCompatActivity> onChangeActivity(activity: Class<Activity>)
}

fun interface OnOpenUrl {
    fun onOpenUrl(externalUrl: String)
}

fun interface OnBackButton {
    fun onBackButton()
}

fun interface OnHideBackButton {
    fun onHideBackButton()
}

fun interface OnShowBackButton {
    fun onShowBackButton()
}

fun interface OnEditButton {
    fun onEditButton(sender: KClass<*>)
}

fun interface OnGoToHome {
    fun onGoToHome()
}

fun interface OnNextStep {
    fun onNextStep(sender: KClass<*>)
}

fun interface OnSkipStep {
    fun onSkipStep(sender: KClass<*>)
}

fun interface OnGoToDetails {
    fun onGoToDetails(id: Long, sender: KClass<*>)
}

fun interface OnGoToProfile {
    fun onGoToProfile(id: Long, sender: KClass<*>)
}

fun interface OnGoToParticipation {
    fun onGoToParticipation()
}

fun interface OnGoToBids {
    fun onGoToBids(id: Long, sender: KClass<*>)
}

fun interface OnRefresh {
    fun onRefresh(id: Long, sender: KClass<*>)
}