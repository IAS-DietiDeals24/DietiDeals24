package com.iasdietideals24.dietideals24.utilities.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.iasdietideals24.dietideals24.utilities.dto.OffertaInversaDto
import com.iasdietideals24.dietideals24.utilities.paging.OffertaInversaPagingSource
import com.iasdietideals24.dietideals24.utilities.services.OffertaInversaService
import kotlinx.coroutines.flow.Flow

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

    fun recuperaOfferteInverse(idAsta: Long): Flow<PagingData<OffertaInversaDto>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { OffertaInversaPagingSource(service, idAsta) }
        ).flow
    }
}