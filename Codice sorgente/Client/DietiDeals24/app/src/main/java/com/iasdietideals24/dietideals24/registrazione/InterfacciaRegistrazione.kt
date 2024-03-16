package com.iasdietideals24.dietideals24.registrazione

import com.iasdietideals24.dietideals24.model.account.Account

interface InterfacciaRegistrazione {
    /**
     * Il metodo verifica se è già stata associata a un altro account dello stesso tipo la
     * medesima email che l'utente vuole utilizzare per registrarsi.
     * @param email email inserita dall'utente.
     * @param tipoAccount tipo di account che l'utente vuole registrare.
     * @return Un [Boolean] con valore true se l'email è già stata usata, false altrimenti.
     */
    fun verificaEmailUsataAccountStessoTipo(email: String, tipoAccount: String): Boolean

    /**
     * Il metodo recupera l'account associato all'email inserita dall'utente se e solo se questo è di tipo differente rispetto a quello che si vuole registrare.
     * @param email email inserita dall'utente.
     * @param tipoAccount tipo di account che l'utente vuole registrare.
     * @return Un [Account] con tutti i dati associati all'account registrato di tipo diverso.
     * Nel caso l'account non esista, viene restituito null.
     */
    fun recuperaAccountTipoDiverso(email: String, tipoAccount: String): Account?
}