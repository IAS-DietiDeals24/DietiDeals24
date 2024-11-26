package com.iasdietideals24.dietideals24.utilities.repositories

import com.iasdietideals24.dietideals24.utilities.dto.AstaSilenziosaDto
import com.iasdietideals24.dietideals24.utilities.services.AstaSilenziosaService
import com.iasdietideals24.dietideals24.utilities.tools.Page

class AstaSilenziosaRepository(private val service: AstaSilenziosaService) {
    suspend fun recuperaAsteCreateSilenziose(
        accountEmail: String,
        size: Long,
        page: Long
    ): Page<AstaSilenziosaDto> {
        return service.recuperaAsteCreateSilenziose(accountEmail, size, page).body()
            ?: Page<AstaSilenziosaDto>()
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

    suspend fun recuperaAsteSilenziose(size: Long, page: Long): Page<AstaSilenziosaDto> {
        return service.recuperaAsteSilenziose(size, page).body() ?: Page<AstaSilenziosaDto>()
    }

    suspend fun ricercaAsteSilenziose(
        ricerca: String,
        filtro: String,
        size: Long,
        page: Long
    ): Page<AstaSilenziosaDto> {
        return service.ricercaAsteSilenziose(ricerca, filtro, size, page).body()
            ?: Page<AstaSilenziosaDto>()
    }

    suspend fun recuperaPartecipazioniSilenziose(
        accountEmail: String,
        size: Long,
        page: Long
    ): Page<AstaSilenziosaDto> {
        return service.recuperaPartecipazioniSilenziose(accountEmail, size, page).body()
            ?: Page<AstaSilenziosaDto>()
    }

    enum class ApiCall { CREATE, TUTTE, RICERCA, PARTECIPAZIONI }
}