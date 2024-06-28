package com.iasdietideals24.dietideals24.registrazione

import com.iasdietideals24.dietideals24.eccezioni.EccezioneEmailUsata
import com.iasdietideals24.dietideals24.model.account.Account

class RegistrazioneDB : InterfacciaRegistrazione {

    @Throws(EccezioneEmailUsata::class)
    override fun verificaEmailUsataAccountStessoTipo(email: String, tipoAccount: String) {
        TODO()
    }

    override fun recuperaAccountTipoDiverso(email: String, tipoAccount: String): Account? {
        TODO()
    }
}