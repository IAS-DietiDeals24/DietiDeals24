package com.iasdietideals24.dietideals24.utilities.classes.data

import java.time.LocalDate
import java.time.LocalTime

data class OffertaRicevuta(
    val idOfferta: Long = 0L,
    val idAsta: Long = 0L,
    val idOfferente: Long = 0L,
    val nomeOfferente: String = "",
    val immagineOfferente: ByteArray = ByteArray(0),
    val offerta: Double = 0.0,
    val dataInvio: LocalDate = LocalDate.MIN,
    val oraInvio: LocalTime = LocalTime.MIN,
    val accettata: Boolean? = null
)