package com.iasdietideals24.dietideals24.utilities

class EccezioneEmailNonValida(private val messaggio: String) : Exception() {

    override val message: String
        get() = messaggio
}
