package com.iasdietideals24.dietideals24.utilities.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.iasdietideals24.dietideals24.utilities.dto.AstaTempoFissoDto
import com.iasdietideals24.dietideals24.utilities.paging.AstaTempoFissoPagingSource
import com.iasdietideals24.dietideals24.utilities.services.AstaTempoFissoService
import kotlinx.coroutines.flow.Flow

class AstaTempoFissoRepository(private val service: AstaTempoFissoService) {
    fun recuperaAsteCreateTempoFisso(accountEmail: String): Flow<PagingData<AstaTempoFissoDto>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                AstaTempoFissoPagingSource(
                    service,
                    email = accountEmail,
                    api = ApiCall.CREATE
                )
            }
        ).flow
    }

    suspend fun creaAstaTempoFisso(asta: AstaTempoFissoDto): AstaTempoFissoDto {
        return service.creaAstaTempoFisso(asta).body() ?: AstaTempoFissoDto()
    }

    suspend fun caricaAstaTempoFisso(idAsta: Long): AstaTempoFissoDto {
        return service.caricaAstaTempoFisso(idAsta).body() ?: AstaTempoFissoDto()
    }

    suspend fun eliminaAstaTempoFisso(idAsta: Long) {
        service.eliminaAstaTempoFisso(idAsta)
    }

    suspend fun aggiornaAstaTempoFisso(asta: AstaTempoFissoDto, idAsta: Long): AstaTempoFissoDto {
        return service.aggiornaAstaTempoFisso(asta, idAsta).body() ?: AstaTempoFissoDto()
    }

    fun recuperaAsteTempoFisso(): Flow<PagingData<AstaTempoFissoDto>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { AstaTempoFissoPagingSource(service, api = ApiCall.TUTTE) }
        ).flow
    }

    fun ricercaAsteTempoFisso(
        ricerca: String,
        filtro: String
    ): Flow<PagingData<AstaTempoFissoDto>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                AstaTempoFissoPagingSource(
                    service,
                    ricerca = ricerca,
                    filtro = filtro,
                    api = ApiCall.RICERCA
                )
            }
        ).flow
    }

    fun recuperaPartecipazioniTempoFisso(email: String): Flow<PagingData<AstaTempoFissoDto>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                AstaTempoFissoPagingSource(
                    service,
                    email = email,
                    api = ApiCall.PARTECIPAZIONI
                )
            }
        ).flow
    }

    enum class ApiCall { CREATE, TUTTE, RICERCA, PARTECIPAZIONI }
}