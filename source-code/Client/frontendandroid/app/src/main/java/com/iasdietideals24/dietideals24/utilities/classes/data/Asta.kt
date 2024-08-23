package com.iasdietideals24.dietideals24.utilities.classes.data

import java.time.LocalDate
import java.time.LocalTime

data class Asta(
    val _idAsta: Long = 0L,
    val _idCreatore: Long = 0L,
    val _tipo: String = "",
    val _dataFine: LocalDate = LocalDate.MIN,
    val _oraFine: LocalTime? = null,
    val _prezzo: Double = 0.0,
    val _immagine: ByteArray = ByteArray(0),
    val _nome: String = "",
    val _categoria: String = "",
    val _descrizione: String = ""
)
