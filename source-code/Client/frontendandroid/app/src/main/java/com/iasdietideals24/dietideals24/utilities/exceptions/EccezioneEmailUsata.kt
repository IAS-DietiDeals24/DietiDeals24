package com.iasdietideals24.dietideals24.utilities.exceptions

class EccezioneEmailUsata(private val messaggio: String) : Exception() {

    override val message: String
        get() = messaggio
}