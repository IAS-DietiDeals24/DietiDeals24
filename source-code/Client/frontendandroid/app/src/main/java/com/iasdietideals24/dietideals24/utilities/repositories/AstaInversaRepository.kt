package com.iasdietideals24.dietideals24.utilities.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.iasdietideals24.dietideals24.utilities.dto.AstaInversaDto
import com.iasdietideals24.dietideals24.utilities.paging.AstaInversaPagingSource
import com.iasdietideals24.dietideals24.utilities.services.AstaInversaService
import kotlinx.coroutines.flow.Flow

class AstaInversaRepository(private val service: AstaInversaService) {
    fun recuperaAsteCreateInverse(accountEmail: String): Flow<PagingData<AstaInversaDto>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                AstaInversaPagingSource(service, email = accountEmail, api = ApiCall.CREATE)
            }
        ).flow
    }

    suspend fun creaAstaInversa(asta: AstaInversaDto): AstaInversaDto {
        return service.creaAstaInversa(asta).body() ?: AstaInversaDto()
    }

    suspend fun caricaAstaInversa(idAsta: Long): AstaInversaDto {
        return service.caricaAstaInversa(idAsta).body() ?: AstaInversaDto()
    }

    suspend fun eliminaAstaInversa(idAsta: Long) {
        service.eliminaAstaInversa(idAsta)
    }

    suspend fun aggiornaAstaInversa(asta: AstaInversaDto, idAsta: Long): AstaInversaDto {
        return service.aggiornaAstaInversa(asta, idAsta).body() ?: AstaInversaDto()
    }

    fun recuperaAsteInverse(): Flow<PagingData<AstaInversaDto>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { AstaInversaPagingSource(service, api = ApiCall.TUTTE) }
        ).flow
    }

    fun ricercaAsteInverse(ricerca: String, filtro: String): Flow<PagingData<AstaInversaDto>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                AstaInversaPagingSource(
                    service, ricerca = ricerca, filtro = filtro, api = ApiCall.RICERCA
                )
            }
        ).flow
    }

    fun recuperaPartecipazioniInverse(email: String): Flow<PagingData<AstaInversaDto>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                AstaInversaPagingSource(
                    service, email = email, api = ApiCall.PARTECIPAZIONI
                )
            }
        ).flow
    }

    enum class ApiCall { CREATE, TUTTE, RICERCA, PARTECIPAZIONI }
}