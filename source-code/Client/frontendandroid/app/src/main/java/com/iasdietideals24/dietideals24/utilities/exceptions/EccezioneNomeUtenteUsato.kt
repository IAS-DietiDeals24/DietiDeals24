package com.iasdietideals24.dietideals24.utilities.exceptions

class EccezioneNomeUtenteUsato(private val messaggio: String) : Exception() {

    override val message: String
        get() = messaggio
}