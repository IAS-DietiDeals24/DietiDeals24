package com.iasdietideals24.dietideals24.utilities.classes.data

import java.time.LocalDate
import java.time.LocalTime

data class DatiAnteprimaAsta(
    val _id: Long = 0L,
    val _tipoAsta: String = "",
    val _dataScadenza: LocalDate = LocalDate.MIN,
    val _oraScadenza: LocalTime = LocalTime.MIN,
    val _foto: ByteArray = ByteArray(0),
    val _nome: String = "",
    val _offerta: Double = 0.0
)