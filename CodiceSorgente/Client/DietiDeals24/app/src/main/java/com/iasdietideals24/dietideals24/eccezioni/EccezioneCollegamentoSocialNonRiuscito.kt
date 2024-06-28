package com.iasdietideals24.dietideals24.eccezioni

class EccezioneCollegamentoSocialNonRiuscito(private val messaggio: String) : Exception() {

    override val message: String
        get() = messaggio
}