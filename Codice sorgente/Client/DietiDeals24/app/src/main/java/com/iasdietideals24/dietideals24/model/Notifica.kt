package com.iasdietideals24.dietideals24.model

import com.iasdietideals24.dietideals24.model.account.Account
import java.sql.Date
import java.sql.Time

class Notifica(
    private var dataInvio: Date,
    private var oraInvio: Time,
    private var messaggio: String,
    private var mittente: Account,
    private var destinatario: MutableList<Account>
) {
    fun getDataInvio(): Date {
        return dataInvio
    }

    fun getOraInvio(): Time {
        return oraInvio
    }

    fun getMessaggio(): String {
        return messaggio
    }

    fun getMittente(): Account {
        return mittente
    }

    fun getDestinatario(): MutableList<Account> {
        return destinatario
    }

    fun setDataInvio(dataInvio: Date) {
        this.dataInvio = dataInvio
    }

    fun setOraInvio(oraInvio: Time) {
        this.oraInvio = oraInvio
    }

    fun setMessaggio(messaggio: String) {
        this.messaggio = messaggio
    }

    fun setMittente(mittente: Account) {
        this.mittente = mittente
    }

    fun setDestinatario(destinatario: MutableList<Account>) {
        this.destinatario = destinatario
    }

    fun addDestinatario(destinatario: Account) {
        this.destinatario.add(destinatario)
    }

    fun removeDestinatario(destinatario: Account) {
        this.destinatario.remove(destinatario)
    }
}