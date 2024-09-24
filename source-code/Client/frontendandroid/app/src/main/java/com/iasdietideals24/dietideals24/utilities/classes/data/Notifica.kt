package com.iasdietideals24.dietideals24.utilities.classes.data

import com.iasdietideals24.dietideals24.utilities.classes.TipoAsta
import java.time.LocalDate
import java.time.LocalTime

data class Notifica(
    val idAsta: Long = 0L,
    val tipoAsta: TipoAsta = TipoAsta.TEMPO_FISSO,
    val idMittente: String = "",
    val mittente: String = "",
    val immagineMittente: ByteArray = ByteArray(0),
    val messaggio: String = "",
    val dataInvio: LocalDate = LocalDate.MIN,
    val oraInvio: LocalTime = LocalTime.MIN
)