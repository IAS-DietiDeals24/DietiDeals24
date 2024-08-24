package com.iasdietideals24.dietideals24.utilities.classes.data

import java.time.LocalDate
import java.time.LocalTime

data class Notifica(
    val _idAsta: Long = 0L,
    val _idMittente: Long = 0L,
    val _mittente: String = "",
    val _immagineMittente: ByteArray = ByteArray(0),
    val _messaggio: String = "",
    val _dataInvio: LocalDate = LocalDate.MIN,
    val _oraInvio: LocalTime = LocalTime.MIN
)