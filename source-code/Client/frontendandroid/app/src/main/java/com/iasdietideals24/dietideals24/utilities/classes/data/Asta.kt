package com.iasdietideals24.dietideals24.utilities.classes.data

import com.iasdietideals24.dietideals24.utilities.classes.TipoAsta
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

data class Asta(
    val idAsta: Long = 0L,
    val idCreatore: String = "",
    val tipo: TipoAsta = TipoAsta.TEMPO_FISSO,
    val dataFine: LocalDate = LocalDate.MIN,
    val oraFine: LocalTime? = LocalTime.MIN,
    val prezzo: BigDecimal = BigDecimal(0.0),
    val immagine: ByteArray = ByteArray(0),
    val nome: String = "",
    val categoria: String = "",
    val descrizione: String = ""
)
