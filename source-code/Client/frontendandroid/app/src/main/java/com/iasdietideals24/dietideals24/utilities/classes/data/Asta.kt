package com.iasdietideals24.dietideals24.utilities.classes.data

import java.time.LocalDate
import java.time.LocalTime

data class Asta(
    private val _idAsta: Long = 0L,
    private val _idCreatore: Long = 0L,
    private val _tipo: String = "",
    private val _dataFine: LocalDate = LocalDate.MIN,
    private val _oraFine: LocalTime? = LocalTime.MIN,
    private val _prezzo: Double = 0.0,
    private val _immagine: ByteArray = ByteArray(0),
    private val _nome: String = "",
    private val _categoria: String = "",
    private val _descrizione: String = ""
)
