package com.iasdietideals24.dietideals24.utilities.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.iasdietideals24.dietideals24.utilities.dto.AstaSilenziosaDto
import com.iasdietideals24.dietideals24.utilities.paging.AstaSilenziosaPagingSource
import com.iasdietideals24.dietideals24.utilities.services.AstaSilenziosaService
import kotlinx.coroutines.flow.Flow

class AstaSilenziosaRepository(private val service: AstaSilenziosaService) {
    fun recuperaAsteCreateSilenziose(accountEmail: String): Flow<PagingData<AstaSilenziosaDto>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                AstaSilenziosaPagingSource(
                    service,
                    email = accountEmail,
                    api = ApiCall.CREATE
                )
            }
        ).flow
    }

    suspend fun creaAstaSilenziosa(asta: AstaSilenziosaDto): AstaSilenziosaDto {
        return service.creaAstaSilenziosa(asta).body() ?: AstaSilenziosaDto()
    }

    suspend fun caricaAstaSilenziosa(idAsta: Long): AstaSilenziosaDto {
        return service.caricaAstaSilenziosa(idAsta).body() ?: AstaSilenziosaDto()
    }

    suspend fun eliminaAstaSilenziosa(idAsta: Long) {
        service.eliminaAstaSilenziosa(idAsta)
    }

    suspend fun aggiornaAstaSilenziosa(asta: AstaSilenziosaDto, idAsta: Long): AstaSilenziosaDto {
        return service.aggiornaAstaSilenziosa(asta, idAsta).body() ?: AstaSilenziosaDto()
    }

    fun recuperaAsteSilenziose(): Flow<PagingData<AstaSilenziosaDto>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { AstaSilenziosaPagingSource(service, api = ApiCall.TUTTE) }
        ).flow
    }

    fun ricercaAsteSilenziose(
        ricerca: String,
        filtro: String
    ): Flow<PagingData<AstaSilenziosaDto>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                AstaSilenziosaPagingSource(
                    service,
                    ricerca = ricerca,
                    filtro = filtro,
                    api = ApiCall.RICERCA
                )
            }
        ).flow
    }

    fun recuperaPartecipazioniSilenziose(email: String): Flow<PagingData<AstaSilenziosaDto>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                AstaSilenziosaPagingSource(
                    service,
                    email = email,
                    api = ApiCall.PARTECIPAZIONI
                )
            }
        ).flow
    }

    enum class ApiCall { CREATE, TUTTE, RICERCA, PARTECIPAZIONI }
}