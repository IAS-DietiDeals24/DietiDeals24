package com.iasdietideals24.dietideals24.utilities.repositories

import com.iasdietideals24.dietideals24.utilities.dto.VenditoreDto
import com.iasdietideals24.dietideals24.utilities.dto.exceptional.PutProfiloDto
import com.iasdietideals24.dietideals24.utilities.services.VenditoreService

class VenditoreRepository(private val service: VenditoreService) {
    suspend fun accountFacebookVenditore(facebookId: String): VenditoreDto {
        return service.accountFacebookVenditore(facebookId).body() ?: VenditoreDto()
    }

    suspend fun accediVenditore(accountEmail: String, accountPassword: String): VenditoreDto {
        return service.accediVenditore(accountEmail, accountPassword).body()
            ?: VenditoreDto()
    }

    suspend fun creazioneAccountVenditore(
        accountEmail: String,
        account: PutProfiloDto
    ): PutProfiloDto {
        return service.creazioneAccountVenditore(accountEmail, account).body()
            ?: PutProfiloDto()
    }

    suspend fun caricaAccountVenditore(accountEmail: String): VenditoreDto {
        return service.caricaAccountVenditore(accountEmail).body() ?: VenditoreDto()
    }

    suspend fun esisteEmailVenditore(accountEmail: String): Boolean {
        return service.esisteEmailVenditore(accountEmail).body() ?: false
    }

    suspend fun associaCreaProfiloVenditore(accountEmail: String): VenditoreDto {
        return service.associaCreaProfiloVenditore(accountEmail).body() ?: VenditoreDto()
    }
}