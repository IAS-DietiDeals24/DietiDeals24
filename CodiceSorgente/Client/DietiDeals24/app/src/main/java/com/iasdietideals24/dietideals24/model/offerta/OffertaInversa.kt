package com.iasdietideals24.dietideals24.model.offerta

import com.iasdietideals24.dietideals24.model.account.AccountVenditore
import com.iasdietideals24.dietideals24.model.asta.AstaInversa
import java.math.BigDecimal
import java.sql.Date
import java.sql.Time

class OffertaInversa(
    dataInvio: Date,
    oraInvio: Time,
    valore: BigDecimal,
    venditoreCollegato: AccountVenditore,

    private var astaRiferimento: AstaInversa
) : OffertaVenditore(dataInvio, oraInvio, valore, venditoreCollegato) {

    fun getAstaRiferimento(): AstaInversa {
        return astaRiferimento
    }

    fun setAstaRiferimento(astaRiferimento: AstaInversa) {
        this.astaRiferimento = astaRiferimento
    }
}