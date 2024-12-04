package com.iasdietideals24.dietideals24.utilities.data

import com.iasdietideals24.dietideals24.utilities.enumerations.CategoriaAsta
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

data class Asta(
    val idAsta: Long = 0L,
    val idCreatore: Long = 0L,
    val tipo: TipoAsta = TipoAsta.TEMPO_FISSO,
    val dataFine: LocalDate = LocalDate.MIN,
    val oraFine: LocalTime? = LocalTime.MIN,
    val prezzo: BigDecimal = BigDecimal("0.00"),
    val immagine: ByteArray = ByteArray(0),
    val nome: String = "",
    val categoria: CategoriaAsta = CategoriaAsta.ND,
    val descrizione: String = ""
)
