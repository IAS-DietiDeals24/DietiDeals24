package com.iasdietideals24.dietideals24.model.offerta

import com.iasdietideals24.dietideals24.model.account.AccountCompratore
import java.math.BigDecimal
import java.sql.Date
import java.sql.Time

abstract class OffertaCompratore(
    dataInvio: Date,
    oraInvio: Time,
    valore: BigDecimal,

    private var compratoreCollegato: AccountCompratore
) : Offerta(dataInvio, oraInvio, valore) {

    fun getCompratoreCollegato(): AccountCompratore {
        return compratoreCollegato
    }

    fun setCompratoreCollegato(compratoreCollegato: AccountCompratore) {
        this.compratoreCollegato = compratoreCollegato
    }
}