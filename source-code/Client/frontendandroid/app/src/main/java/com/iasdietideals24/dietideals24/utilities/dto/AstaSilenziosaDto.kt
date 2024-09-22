package com.iasdietideals24.dietideals24.utilities.dto

import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto
import com.iasdietideals24.dietideals24.utilities.dto.shallows.NotificaShallowDto
import com.iasdietideals24.dietideals24.utilities.dto.shallows.OffertaShallowDto
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import java.time.LocalDate
import java.time.LocalTime

@Getter
@Setter
@NoArgsConstructor
class AstaSilenziosaDto {
    private val idAsta: Long? = null

    private val categoria: String? = null

    private val nome: String? = null

    private val descrizione: String? = null

    private val dataScadenza: LocalDate? = null

    private val oraScadenza: LocalTime? = null

    private val immagine: ByteArray? = null

    private val notificheAssociateShallow: Set<NotificaShallowDto>? = null

    private val proprietarioShallow: AccountShallowDto? = null

    private val offerteRicevuteShallow: Set<OffertaShallowDto>? = null
}
