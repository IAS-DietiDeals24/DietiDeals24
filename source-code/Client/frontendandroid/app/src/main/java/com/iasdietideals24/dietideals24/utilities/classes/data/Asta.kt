package com.iasdietideals24.dietideals24.utilities.classes.data

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

data class Asta(
    private val _idAsta: Long = 0L,
    private val _idCreatore: String = "",
    private val _tipo: String = "",
    private val _dataFine: LocalDate = LocalDate.MIN,
    private val _oraFine: LocalTime? = LocalTime.MIN,
    private val _prezzo: BigDecimal = BigDecimal(0.0),
    private val _immagine: ByteArray = ByteArray(0),
    private val _nome: String = "",
    private val _categoria: String = "",
    private val _descrizione: String = ""
)
