package com.iasdietideals24.dietideals24.model.offerta

import com.iasdietideals24.dietideals24.model.account.AccountCompratore
import com.iasdietideals24.dietideals24.model.asta.AstaTempoFisso
import java.math.BigDecimal
import java.sql.Date
import java.sql.Time

class OffertaTempoFisso(
    dataInvio: Date,
    oraInvio: Time,
    valore: BigDecimal,
    compratoreCollegato: AccountCompratore,

    private var astaRiferimento: AstaTempoFisso
) : OffertaCompratore(dataInvio, oraInvio, valore, compratoreCollegato) {

    fun getAstaRiferimento(): AstaTempoFisso {
        return astaRiferimento
    }

    fun setAstaRiferimento(astaRiferimento: AstaTempoFisso) {
        this.astaRiferimento = astaRiferimento
    }
}