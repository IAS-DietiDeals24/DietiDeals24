package com.iasdietideals24.dietideals24.utilities.data

import com.iasdietideals24.dietideals24.utilities.dto.OffertaInversaDto
import com.iasdietideals24.dietideals24.utilities.dto.OffertaSilenziosaDto
import com.iasdietideals24.dietideals24.utilities.dto.OffertaTempoFissoDto
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AstaShallowDto
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

data class Offerta(
    val idOfferta: Long = 0L,
    val idAsta: Long = 0L,
    val idOfferente: String = "",
    val offerta: BigDecimal = BigDecimal(0.0),
    val dataInvio: LocalDate = LocalDate.MIN,
    val oraInvio: LocalTime = LocalTime.MIN
) {
    fun toOffertaTempoFisso(): OffertaTempoFissoDto {
        return OffertaTempoFissoDto(
            idOfferta,
            dataInvio,
            oraInvio,
            offerta,
            AccountShallowDto(idOfferente, TipoAccount.COMPRATORE.name),
            AstaShallowDto(idAsta, TipoAccount.VENDITORE.name, TipoAsta.TEMPO_FISSO.name)
        )
    }

    fun toOffertaSilenziosa(): OffertaSilenziosaDto {
        return OffertaSilenziosaDto(
            idOfferta,
            dataInvio,
            oraInvio,
            offerta,
            AccountShallowDto(idOfferente, TipoAccount.COMPRATORE.name),
            null,
            AstaShallowDto(idAsta, TipoAccount.VENDITORE.name, TipoAsta.SILENZIOSA.name)
        )
    }

    fun toOffertaInversa(): OffertaInversaDto {
        return OffertaInversaDto(
            idOfferta,
            dataInvio,
            oraInvio,
            offerta,
            AccountShallowDto(idOfferente, TipoAccount.VENDITORE.name),
            AstaShallowDto(idAsta, TipoAccount.COMPRATORE.name, TipoAsta.INVERSA.name)
        )
    }
}