package com.iasdietideals24.dietideals24.model.offerta

import com.iasdietideals24.dietideals24.model.account.AccountCompratore
import com.iasdietideals24.dietideals24.model.asta.AstaSilenziosa
import java.math.BigDecimal
import java.sql.Date
import java.sql.Time

class OffertaSilenziosa(
    dataInvio: Date,
    oraInvio: Time,
    valore: BigDecimal,
    compratoreCollegato: AccountCompratore,

    private var isAccettata: Boolean,
    private var astaRiferimento: AstaSilenziosa
) : OffertaCompratore(dataInvio, oraInvio, valore, compratoreCollegato) {

    fun isAccettata(): Boolean {
        return isAccettata
    }

    fun setAccettata(accettata: Boolean) {
        isAccettata = accettata
    }

    fun getAstaRiferimento(): AstaSilenziosa {
        return astaRiferimento
    }

    fun setAstaRiferimento(astaRiferimento: AstaSilenziosa) {
        this.astaRiferimento = astaRiferimento
    }
}