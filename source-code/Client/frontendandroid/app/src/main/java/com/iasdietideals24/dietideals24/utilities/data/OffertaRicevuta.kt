package com.iasdietideals24.dietideals24.utilities.data

import com.iasdietideals24.dietideals24.utilities.enumerations.StatoOfferta
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

data class OffertaRicevuta(
    val idOfferta: Long = 0L,
    val idAsta: Long = 0L,
    val tipoAsta: TipoAsta = TipoAsta.TEMPO_FISSO,
    val idOfferente: String = "",
    val nomeOfferente: String = "",
    val immagineOfferente: ByteArray = ByteArray(0),
    val offerta: BigDecimal = BigDecimal(0.0),
    val dataInvio: LocalDate = LocalDate.MIN,
    val oraInvio: LocalTime = LocalTime.MIN,
    val stato: StatoOfferta? = StatoOfferta.PENDING
)