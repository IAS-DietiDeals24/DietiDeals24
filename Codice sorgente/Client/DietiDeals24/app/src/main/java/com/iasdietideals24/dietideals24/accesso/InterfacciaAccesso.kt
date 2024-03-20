package com.iasdietideals24.dietideals24.accesso

import com.iasdietideals24.dietideals24.eccezioni.EccezioneAccountNonEsistente
import com.iasdietideals24.dietideals24.model.account.Account


fun interface InterfacciaAccesso {

    /**
     * Verifica se esiste un account registrato con l'email e la password inserite dall'utente.
     * @param email email inserita dall'utente, non nulla o vuota.
     * @param password password inserita dall'utente, non nulla o vuota.
     * @return un account con tutti i dati associati all'account registrato.
     * @throws [IllegalArgumentException] se l'email o la password sono nulle o vuote o se i parametri non sono [String].
     * @throws [EccezioneAccountNonEsistente] se non esiste un account registrato con l'email e la password inserite.
     * @see Account
     */
    @Throws(EccezioneAccountNonEsistente::class)
    fun recuperaAccount(email: String, password: String): Account
}