package com.iasdietideals24.dietideals24.model.offerta

import java.math.BigDecimal
import java.sql.Date
import java.sql.Time

abstract class Offerta(
    private var dataInvio: Date,
    private var oraInvio: Time,
    private var valore: BigDecimal
) {
    fun getDataInvio(): Date {
        return dataInvio
    }

    fun getOraInvio(): Time {
        return oraInvio
    }

    fun getValore(): BigDecimal {
        return valore
    }

    fun setDataInvio(dataInvio: Date) {
        this.dataInvio = dataInvio
    }

    fun setOraInvio(oraInvio: Time) {
        this.oraInvio = oraInvio
    }

    fun setValore(valore: BigDecimal) {
        this.valore = valore
    }
}