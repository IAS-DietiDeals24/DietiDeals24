package com.iasdietideals24.dietideals24.utilities.repositories

import com.iasdietideals24.dietideals24.utilities.dto.CompratoreDto
import com.iasdietideals24.dietideals24.utilities.services.CompratoreService

class CompratoreRepository(private val service: CompratoreService) {

    suspend fun accediCompratore(accountEmail: String): CompratoreDto {
        return service.accediCompratore(accountEmail).body()?.content?.singleOrNull()
            ?: CompratoreDto()
    }

    suspend fun caricaAccountCompratore(idAccount: Long): CompratoreDto {
        return service.caricaAccountCompratore(idAccount).body() ?: CompratoreDto()
    }
}