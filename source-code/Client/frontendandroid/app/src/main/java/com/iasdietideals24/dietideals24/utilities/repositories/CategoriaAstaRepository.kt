package com.iasdietideals24.dietideals24.utilities.repositories

import com.iasdietideals24.dietideals24.utilities.dto.CategoriaAstaDto
import com.iasdietideals24.dietideals24.utilities.services.CategoriaAstaService

class CategoriaAstaRepository(private val service: CategoriaAstaService) {
    suspend fun recuperaCategorieAsta(): List<CategoriaAstaDto> {
        return service.recuperaCategorieAste(20, 0).body()?.content ?: mutableListOf()
    }
}