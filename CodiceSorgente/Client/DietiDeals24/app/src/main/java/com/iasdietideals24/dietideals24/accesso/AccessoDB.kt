package com.iasdietideals24.dietideals24.accesso

import com.iasdietideals24.dietideals24.eccezioni.EccezioneAccountNonEsistente
import com.iasdietideals24.dietideals24.model.account.Account


class AccessoDB : InterfacciaAccesso {

    @Throws(EccezioneAccountNonEsistente::class)
    override fun recuperaAccount(email: String, password: String): Account {
        TODO()
    }
}