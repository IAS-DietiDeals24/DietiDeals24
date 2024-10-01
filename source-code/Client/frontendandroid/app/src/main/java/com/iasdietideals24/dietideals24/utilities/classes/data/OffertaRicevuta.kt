package com.iasdietideals24.dietideals24.utilities.classes.data

import com.iasdietideals24.dietideals24.utilities.classes.StatoOfferta
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

data class OffertaRicevuta(
    val idOfferta: Long = 0L,
    val idAsta: Long = 0L,
    val idOfferente: String = "",
    val nomeOfferente: String = "",
    val immagineOfferente: ByteArray = ByteArray(0),
    val offerta: BigDecimal = BigDecimal(0.0),
    val dataInvio: LocalDate = LocalDate.MIN,
    val oraInvio: LocalTime = LocalTime.MIN,
    val stato: StatoOfferta = StatoOfferta.PENDING
)