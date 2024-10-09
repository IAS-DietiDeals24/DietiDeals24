package com.iasdietideals24.dietideals24.utilities.data

import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

data class AnteprimaAsta(
    val id: Long = 0L,
    val tipoAsta: TipoAsta = TipoAsta.TEMPO_FISSO,
    val dataScadenza: LocalDate = LocalDate.MIN,
    val oraScadenza: LocalTime = LocalTime.MIN,
    val foto: ByteArray = ByteArray(0),
    val nome: String = "",
    val offerta: BigDecimal = BigDecimal(0.0)
)