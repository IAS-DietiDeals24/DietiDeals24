package com.iasdietideals24.dietideals24.utilities.repositories

import com.iasdietideals24.dietideals24.utilities.dto.ProfiloDto
import com.iasdietideals24.dietideals24.utilities.services.ProfiloService

class ProfiloRepository(private val service: ProfiloService) {
    suspend fun caricaProfiloDaAccount(accountEmail: String): ProfiloDto {
        return service.caricaProfiloDaAccount(accountEmail).body() ?: ProfiloDto()
    }

    suspend fun esisteNomeUtente(nomeUtente: String): Boolean {
        return service.esisteNomeUtente(nomeUtente).body() ?: false
    }

    suspend fun aggiornaProfilo(profilo: ProfiloDto, nomeUtente: String): ProfiloDto {
        return service.aggiornaProfilo(profilo, nomeUtente).body() ?: ProfiloDto()
    }
}