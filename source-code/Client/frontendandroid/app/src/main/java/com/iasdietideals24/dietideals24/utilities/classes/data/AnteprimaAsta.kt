package com.iasdietideals24.dietideals24.utilities.classes.data

import java.time.LocalDate
import java.time.LocalTime

data class AnteprimaAsta(
    val id: Long = 0L,
    val tipoAsta: String = "",
    val dataScadenza: LocalDate = LocalDate.MIN,
    val oraScadenza: LocalTime = LocalTime.MIN,
    val foto: ByteArray = ByteArray(0),
    val nome: String = "",
    val offerta: Double = 0.0
)