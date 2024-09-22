package com.iasdietideals24.dietideals24.utilities.dto

import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AstaShallowDto
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import java.time.LocalDate
import java.time.LocalTime

@Getter
@Setter
@NoArgsConstructor
class NotificaDto {
    private val idNotifica: Long? = null

    private val dataInvio: LocalDate? = null

    private val oraInvio: LocalTime? = null

    private val messaggio: String? = null

    private val mittenteShallow: AccountShallowDto? = null

    private val destinatariShallow: Set<AccountShallowDto>? = null

    private val astaAssociataShallow: AstaShallowDto? = null
}
