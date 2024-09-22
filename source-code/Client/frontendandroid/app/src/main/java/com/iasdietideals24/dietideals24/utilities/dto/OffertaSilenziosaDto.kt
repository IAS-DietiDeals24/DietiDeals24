package com.iasdietideals24.dietideals24.utilities.dto

import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AstaShallowDto
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

@Getter
@Setter
@NoArgsConstructor
class OffertaSilenziosaDto {
    private val idOfferta: Long? = null

    private val dataInvio: LocalDate? = null

    private val oraInvio: LocalTime? = null

    private val valore: BigDecimal? = null

    private val compratoreCollegatoShallow: AccountShallowDto? = null

    private val isAccettata: Boolean? = null

    private val astaRiferimentoShallow: AstaShallowDto? = null
}
