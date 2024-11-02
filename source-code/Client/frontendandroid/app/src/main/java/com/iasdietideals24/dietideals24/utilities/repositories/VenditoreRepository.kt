package com.iasdietideals24.dietideals24.utilities.repositories

import com.iasdietideals24.dietideals24.utilities.dto.VenditoreDto
import com.iasdietideals24.dietideals24.utilities.services.VenditoreService

class VenditoreRepository(private val service: VenditoreService) {
    suspend fun accountFacebookVenditore(idFacebook: String): VenditoreDto {
        return service.accountFacebookVenditore(idFacebook).body() ?: VenditoreDto()
    }

    suspend fun accediVenditore(accountEmail: String, accountPassword: String): VenditoreDto {
        return service.accediVenditore(accountEmail, accountPassword).body()
            ?: VenditoreDto()
    }

    suspend fun caricaAccountVenditore(accountEmail: String): VenditoreDto {
        return service.caricaAccountVenditore(accountEmail).body() ?: VenditoreDto()
    }
}