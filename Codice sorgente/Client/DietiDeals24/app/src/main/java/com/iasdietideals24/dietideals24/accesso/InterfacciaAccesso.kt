package com.iasdietideals24.dietideals24.accesso

import com.iasdietideals24.dietideals24.model.account.Account

fun interface InterfacciaAccesso {
    /**
     * Il metodo verifica se esiste un account registrato con l'email e la password inserite dall'utente.
     * @param email email inserita dall'utente.
     * @param password password inserita dall'utente.
     * @return Un [Account] con tutti i dati associati all'account registrato.
     * Nel caso l'account non esista, viene restituito null.
     */
    fun recuperaAccount(email: String, password: String): Account?
}