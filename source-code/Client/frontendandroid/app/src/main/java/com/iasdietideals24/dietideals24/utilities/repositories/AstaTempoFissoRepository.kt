package com.iasdietideals24.dietideals24.utilities.repositories

import com.iasdietideals24.dietideals24.utilities.dto.AstaTempoFissoDto
import com.iasdietideals24.dietideals24.utilities.services.AstaTempoFissoService
import com.iasdietideals24.dietideals24.utilities.tools.Page

class AstaTempoFissoRepository(private val service: AstaTempoFissoService) {
    suspend fun recuperaAsteCreateTempoFisso(
        accountEmail: String,
        size: Long,
        page: Long
    ): Page<AstaTempoFissoDto> {
        return service.recuperaAsteCreateTempoFisso(accountEmail, size, page).body()
            ?: Page<AstaTempoFissoDto>()
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

    suspend fun recuperaAsteTempoFisso(size: Long, page: Long): Page<AstaTempoFissoDto> {
        return service.recuperaAsteTempoFisso(size, page).body() ?: Page<AstaTempoFissoDto>()
    }

    suspend fun ricercaAsteTempoFisso(
        ricerca: String,
        filtro: String,
        size: Long,
        page: Long
    ): Page<AstaTempoFissoDto> {
        return service.ricercaAsteTempoFisso(ricerca, filtro, size, page).body()
            ?: Page<AstaTempoFissoDto>()
    }

    suspend fun recuperaPartecipazioniTempoFisso(
        accountEmail: String,
        size: Long,
        page: Long
    ): Page<AstaTempoFissoDto> {
        return service.recuperaPartecipazioniTempoFisso(accountEmail, size, page).body()
            ?: Page<AstaTempoFissoDto>()
    }

    enum class ApiCall { CREATE, TUTTE, RICERCA, PARTECIPAZIONI }
}