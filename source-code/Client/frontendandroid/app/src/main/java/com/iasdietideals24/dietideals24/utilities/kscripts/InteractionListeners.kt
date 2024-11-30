package com.iasdietideals24.dietideals24.utilities.kscripts

import androidx.appcompat.app.AppCompatActivity
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta
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

interface OnEditButton {
    fun onEditButton(id: Long = 0L, tipo: TipoAsta = TipoAsta.TEMPO_FISSO, sender: KClass<*>)
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
    fun onGoToDetails(id: Long, tipo: TipoAsta, sender: KClass<*>)
}

fun interface OnGoToProfile {
    fun onGoToProfile(id: Long, sender: KClass<*>)
}

fun interface OnGoToParticipation {
    fun onGoToParticipation()
}

fun interface OnGoToCreatedAuctions {
    fun onGoToCreatedAuctions()
}

fun interface OnGoToHelp {
    fun onGoToHelp()
}

fun interface OnGoToBids {
    fun onGoToBids(id: Long, tipo: TipoAsta, sender: KClass<*>)
}

interface OnRefresh {
    fun onRefresh(id: Long = 0L, tipo: TipoAsta = TipoAsta.TEMPO_FISSO, sender: KClass<*>)
}

fun interface OnHideMaterialDivider {
    fun onHideMaterialDivider(hide: Boolean)
}