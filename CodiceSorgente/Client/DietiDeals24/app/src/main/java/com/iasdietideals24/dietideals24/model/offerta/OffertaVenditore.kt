package com.iasdietideals24.dietideals24.model.offerta

import com.iasdietideals24.dietideals24.model.account.AccountVenditore
import java.math.BigDecimal
import java.sql.Date
import java.sql.Time

abstract class OffertaVenditore protected constructor(
    dataInvio: Date,
    oraInvio: Time,
    valore: BigDecimal,

    private var venditoreCollegato: AccountVenditore
) : Offerta(dataInvio, oraInvio, valore) {

    fun getVenditoreCollegato(): AccountVenditore {
        return venditoreCollegato
    }

    fun setVenditoreCollegato(venditoreCollegato: AccountVenditore) {
        this.venditoreCollegato = venditoreCollegato
    }
}