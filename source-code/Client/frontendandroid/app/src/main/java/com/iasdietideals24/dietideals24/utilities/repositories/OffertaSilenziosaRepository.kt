package com.iasdietideals24.dietideals24.utilities.repositories

import com.iasdietideals24.dietideals24.utilities.dto.OffertaSilenziosaDto
import com.iasdietideals24.dietideals24.utilities.services.OffertaSilenziosaService
import com.iasdietideals24.dietideals24.utilities.tools.Page

class OffertaSilenziosaRepository(private val service: OffertaSilenziosaService) {
    suspend fun recuperaOffertaPersonalePiuAltaSilenziosa(
        idAsta: Long,
        idAccount: Long
    ): OffertaSilenziosaDto {
        return service.recuperaOffertaPersonalePiuAltaSilenziosa(idAsta, idAccount)
            .body() ?: OffertaSilenziosaDto()
    }

    suspend fun inviaOffertaSilenziosa(offerta: OffertaSilenziosaDto): OffertaSilenziosaDto {
        return service.inviaOffertaSilenziosa(offerta).body() ?: OffertaSilenziosaDto()
    }

    suspend fun recuperaOfferteSilenziose(
        idAsta: Long,
        size: Long,
        page: Long
    ): Page<OffertaSilenziosaDto> {
        return service.recuperaOfferteSilenziose(idAsta, size, page).body()
            ?: Page<OffertaSilenziosaDto>()
    }

    suspend fun accettaOfferta(
        offerta: OffertaSilenziosaDto,
        idOfferta: Long
    ): OffertaSilenziosaDto {
        return service.accettaOfferta(offerta, idOfferta).body() ?: OffertaSilenziosaDto()
    }

    suspend fun rifiutaOfferta(
        offerta: OffertaSilenziosaDto,
        idOfferta: Long
    ): OffertaSilenziosaDto {
        return service.rifiutaOfferta(offerta, idOfferta).body() ?: OffertaSilenziosaDto()
    }
}