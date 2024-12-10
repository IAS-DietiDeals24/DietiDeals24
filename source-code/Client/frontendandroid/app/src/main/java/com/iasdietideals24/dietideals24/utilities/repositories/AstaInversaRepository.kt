package com.iasdietideals24.dietideals24.utilities.repositories

import com.iasdietideals24.dietideals24.utilities.dto.AstaInversaDto
import com.iasdietideals24.dietideals24.utilities.services.AstaInversaService
import com.iasdietideals24.dietideals24.utilities.tools.Page

class AstaInversaRepository(private val service: AstaInversaService) {
    suspend fun recuperaAsteCreateInverse(
        idAccount: Long,
        size: Long,
        page: Long
    ): Page<AstaInversaDto> {
        return service.recuperaAsteCreateInverse(idAccount, size, page).body()
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

    suspend fun recuperaAsteInverse(idAccount: Long, size: Long, page: Long): Page<AstaInversaDto> {
        return service.recuperaAsteInverse(idAccount, size, page).body() ?: Page<AstaInversaDto>()
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
        idAccount: Long,
        size: Long,
        page: Long
    ): Page<AstaInversaDto> {
        return service.recuperaPartecipazioniInverse(idAccount, size, page).body()
            ?: Page<AstaInversaDto>()
    }

    enum class ApiCall { CREATE, TUTTE, RICERCA, PARTECIPAZIONI }
}