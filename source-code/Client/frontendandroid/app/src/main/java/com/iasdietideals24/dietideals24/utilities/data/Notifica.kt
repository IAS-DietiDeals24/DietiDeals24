package com.iasdietideals24.dietideals24.utilities.data

import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta
import java.time.LocalDate
import java.time.LocalTime

data class Notifica(
    val idAsta: Long = 0L,
    val tipoAsta: TipoAsta = TipoAsta.TEMPO_FISSO,
    val idMittente: Long = 0L,
    val mittente: String = "",
    val immagineMittente: ByteArray = ByteArray(0),
    val messaggio: String = "",
    val dataInvio: LocalDate = LocalDate.MIN,
    val oraInvio: LocalTime = LocalTime.MIN
)