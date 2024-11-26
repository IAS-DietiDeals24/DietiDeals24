package com.iasdietideals24.dietideals24.utilities.repositories

import com.iasdietideals24.dietideals24.utilities.dto.OffertaInversaDto
import com.iasdietideals24.dietideals24.utilities.services.OffertaInversaService
import com.iasdietideals24.dietideals24.utilities.tools.Page

class OffertaInversaRepository(private val service: OffertaInversaService) {
    suspend fun recuperaOffertaPiuBassa(idAsta: Long): OffertaInversaDto {
        return service.recuperaOffertaPiuBassa(idAsta).body() ?: OffertaInversaDto()
    }

    suspend fun recuperaOffertaPersonalePiuBassaInversa(
        idAsta: Long,
        accountEmail: String
    ): OffertaInversaDto {
        return service.recuperaOffertaPersonalePiuBassaInversa(idAsta, accountEmail)
            .body() ?: OffertaInversaDto()
    }

    suspend fun inviaOffertaInversa(offerta: OffertaInversaDto, idAsta: Long): OffertaInversaDto {
        return service.inviaOffertaInversa(offerta, idAsta).body() ?: OffertaInversaDto()
    }

    suspend fun recuperaOfferteInverse(
        idAsta: Long,
        size: Long,
        page: Long
    ): Page<OffertaInversaDto> {
        return service.recuperaOfferteInverse(idAsta, size, page).body()
            ?: Page<OffertaInversaDto>()
    }
}