package com.iasdietideals24.dietideals24.utilities.classes.data

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

data class Offerta(
    val idAsta: Long = 0L,
    val idOfferente: Long = 0L,
    val offerta: BigDecimal = BigDecimal(0.0),
    val dataInvio: LocalDate = LocalDate.MIN,
    val oraInvio: LocalTime = LocalTime.MIN
)