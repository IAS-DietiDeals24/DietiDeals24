package com.iasdietideals24.dietideals24.registrazione

import com.iasdietideals24.dietideals24.model.account.Account

class RegistrazioneDB : InterfacciaRegistrazione {

    override fun verificaEmailUsataAccountStessoTipo(email: String, tipoAccount: String): Boolean {
        //TODO

        return false
    }

    override fun recuperaAccountTipoDiverso(email: String, tipoAccount: String): Account? {
        //TODO

        return null
    }
}