package com.iasdietideals24.dietideals24.utilities

class EccezionePasswordNonSicura(private val messaggio: String) : Exception() {

    override val message: String
        get() = messaggio

}
