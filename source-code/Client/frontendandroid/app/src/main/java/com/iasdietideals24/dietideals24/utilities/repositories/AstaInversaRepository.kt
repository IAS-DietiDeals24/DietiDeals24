package com.iasdietideals24.dietideals24.utilities.repositories

import com.iasdietideals24.dietideals24.utilities.dto.AstaInversaDto
import com.iasdietideals24.dietideals24.utilities.services.AstaInversaService
import com.iasdietideals24.dietideals24.utilities.tools.Page

class AstaInversaRepository(private val service: AstaInversaService) {
    suspend fun recuperaAsteCreateInverse(
        accountEmail: String,
        size: Long,
        page: Long
    ): Page<AstaInversaDto> {
        return service.recuperaAsteCreateInverse(accountEmail, size, page).body()
            ?: Page<AstaInversaDto>()
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

    suspend fun recuperaAsteInverse(size: Long, page: Long): Page<AstaInversaDto> {
        return service.recuperaAsteInverse(size, page).body() ?: Page<AstaInversaDto>()
    }

    suspend fun ricercaAsteInverse(
        ricerca: String,
        filtro: String,
        size: Long,
        page: Long
    ): Page<AstaInversaDto> {
        return service.ricercaAsteInverse(ricerca, filtro, size, page).body()
            ?: Page<AstaInversaDto>()
    }

    suspend fun recuperaPartecipazioniInverse(
        accountEmail: String,
        size: Long,
        page: Long
    ): Page<AstaInversaDto> {
        return service.recuperaPartecipazioniInverse(accountEmail, size, page).body()
            ?: Page<AstaInversaDto>()
    }

    enum class ApiCall { CREATE, TUTTE, RICERCA, PARTECIPAZIONI }
}