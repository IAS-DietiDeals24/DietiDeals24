package com.iasdietideals24.dietideals24.utilities.interfaces

import kotlin.reflect.KClass

fun interface OnGoToDetails {
    fun onGoToDetails(id: Long, sender: KClass<*>)
}

fun interface OnGoToProfile {
    fun onGoToProfile(id: Long, sender: KClass<*>)
}