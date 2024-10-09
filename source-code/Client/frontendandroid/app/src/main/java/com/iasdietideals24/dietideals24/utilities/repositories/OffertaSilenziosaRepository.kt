package com.iasdietideals24.dietideals24.utilities.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.iasdietideals24.dietideals24.utilities.dto.OffertaSilenziosaDto
import com.iasdietideals24.dietideals24.utilities.paging.OffertaSilenziosaPagingSource
import com.iasdietideals24.dietideals24.utilities.services.OffertaSilenziosaService
import kotlinx.coroutines.flow.Flow

class OffertaSilenziosaRepository(private val service: OffertaSilenziosaService) {
    suspend fun recuperaOffertaPersonalePiuAltaSilenziosa(
        idAsta: Long,
        accountEmail: String
    ): OffertaSilenziosaDto {
        return service.recuperaOffertaPersonalePiuAltaSilenziosa(idAsta, accountEmail)
            .body() ?: OffertaSilenziosaDto()
    }

    suspend fun inviaOffertaSilenziosa(
        offerta: OffertaSilenziosaDto,
        idAsta: Long
    ): OffertaSilenziosaDto {
        return service.inviaOffertaSilenziosa(offerta, idAsta).body()
            ?: OffertaSilenziosaDto()
    }

    fun recuperaOfferteSilenziose(idAsta: Long): Flow<PagingData<OffertaSilenziosaDto>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { OffertaSilenziosaPagingSource(service, idAsta) }
        ).flow
    }

    suspend fun accettaOfferta(offerta: OffertaSilenziosaDto, idOfferta: Long): Boolean {
        return service.accettaOfferta(offerta, idOfferta).body() ?: false
    }

    suspend fun rifiutaOfferta(offerta: OffertaSilenziosaDto, idOfferta: Long): Boolean {
        return service.rifiutaOfferta(offerta, idOfferta).body() ?: false
    }
}