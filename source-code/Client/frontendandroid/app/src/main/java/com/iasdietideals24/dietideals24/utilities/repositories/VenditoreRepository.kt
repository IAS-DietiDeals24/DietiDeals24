package com.iasdietideals24.dietideals24.utilities.repositories

import com.iasdietideals24.dietideals24.utilities.dto.VenditoreDto
import com.iasdietideals24.dietideals24.utilities.services.VenditoreService

class VenditoreRepository(private val service: VenditoreService) {

    suspend fun accediVenditore(accountEmail: String): VenditoreDto {
        return service.accediVenditore(accountEmail).body()?.content?.singleOrNull()
            ?: VenditoreDto()
    }

    suspend fun caricaAccountVenditore(idAccount: Long): VenditoreDto {
        return service.caricaAccountVenditore(idAccount).body() ?: VenditoreDto()
    }

    suspend fun creaAccountVenditore(account: VenditoreDto): VenditoreDto {
        return service.creaAccountVenditore(account).body() ?: VenditoreDto()
    }
}