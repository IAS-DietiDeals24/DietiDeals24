package com.iasdietideals24.dietideals24.utilities.repositories

import com.iasdietideals24.dietideals24.utilities.dto.OffertaTempoFissoDto
import com.iasdietideals24.dietideals24.utilities.services.OffertaTempoFissoService
import com.iasdietideals24.dietideals24.utilities.tools.Page

class OffertaTempoFissoRepository(private val service: OffertaTempoFissoService) {
    suspend fun recuperaOffertaPiuAlta(idAsta: Long): OffertaTempoFissoDto {
        return service.recuperaOffertaPiuAlta(idAsta).body() ?: OffertaTempoFissoDto()
    }

    suspend fun recuperaOffertaPersonalePiuAltaTempoFisso(
        idAsta: Long,
        accountEmail: String
    ): OffertaTempoFissoDto {
        return service.recuperaOffertaPersonalePiuAltaTempoFisso(idAsta, accountEmail)
            .body() ?: OffertaTempoFissoDto()
    }

    suspend fun inviaOffertaTempoFisso(
        offerta: OffertaTempoFissoDto,
        idAsta: Long
    ): OffertaTempoFissoDto {
        return service.inviaOffertaTempoFisso(offerta, idAsta).body()
            ?: OffertaTempoFissoDto()
    }

    suspend fun recuperaOfferteTempoFisso(
        idAsta: Long,
        size: Long,
        page: Long
    ): Page<OffertaTempoFissoDto> {
        return service.recuperaOfferteTempoFisso(idAsta, size, page).body()
            ?: Page<OffertaTempoFissoDto>()
    }
}