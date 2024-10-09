package com.iasdietideals24.dietideals24.utilities.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.iasdietideals24.dietideals24.utilities.dto.OffertaTempoFissoDto
import com.iasdietideals24.dietideals24.utilities.paging.OffertaTempoFissoPagingSource
import com.iasdietideals24.dietideals24.utilities.services.OffertaTempoFissoService
import kotlinx.coroutines.flow.Flow

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

    fun recuperaOfferteTempoFisso(idAsta: Long): Flow<PagingData<OffertaTempoFissoDto>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { OffertaTempoFissoPagingSource(service, idAsta) }
        ).flow
    }
}