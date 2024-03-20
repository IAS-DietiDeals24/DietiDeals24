package com.iasdietideals24.dietideals24.registrazione

import com.iasdietideals24.dietideals24.eccezioni.EccezioneEmailUsata
import com.iasdietideals24.dietideals24.model.account.Account

interface InterfacciaRegistrazione {
    /**
     * Verifica se è già stata associata a un altro account dello stesso tipo la medesima email che l'utente vuole utilizzare per registrarsi.
     * @param email email inserita dall'utente, non nulla o vuota.
     * @param tipoAccount tipo di account che l'utente vuole registrare, non nullo o vuoto.
     * @throws [EccezioneEmailUsata] se l'email è già stata usata per un account dello stesso tipo.
     * @throws [IllegalArgumentException] se l'email o il tipoAccount sono nulli o vuoti o se i parametri non sono del giusto tipo.
     */
    @Throws(EccezioneEmailUsata::class)
    fun verificaEmailUsataAccountStessoTipo(email: String, tipoAccount: String)

    /**
     * Il metodo recupera l'account associato all'email inserita dall'utente se e solo se questo è di tipo differente rispetto a quello che si vuole registrare.
     * @param email email inserita dall'utente, non nulla o vuota.
     * @param tipoAccount tipo di account che l'utente vuole registrare, non nullo o vuoto.
     * @return un account con tutti i dati associati all'account registrato di tipo diverso.
     * Nel caso in cui tale account non esista, restituisce null.
     * @throws [IllegalArgumentException] se l'email o il tipoAccount sono nulli o vuoti o se i parametri non sono del giusto tipo.
     * @see Account
     */
    fun recuperaAccountTipoDiverso(email: String, tipoAccount: String): Account?
}