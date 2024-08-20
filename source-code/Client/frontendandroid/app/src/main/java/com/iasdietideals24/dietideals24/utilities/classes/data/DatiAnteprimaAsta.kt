package com.iasdietideals24.dietideals24.utilities.classes.data

import java.sql.Date

data class DatiAnteprimaAsta(
    val _id: Long = 0L,
    val _tipoAsta: String = "",
    val _dataScadenza: Date = Date(0),
    val _foto: ByteArray = ByteArray(0),
    val _nome: String = "",
    val _offerta: Double = 0.0
)