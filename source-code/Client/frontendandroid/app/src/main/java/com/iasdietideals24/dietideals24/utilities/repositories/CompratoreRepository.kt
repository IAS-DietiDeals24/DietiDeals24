package com.iasdietideals24.dietideals24.utilities.repositories

import com.iasdietideals24.dietideals24.utilities.dto.CompratoreDto
import com.iasdietideals24.dietideals24.utilities.dto.exceptional.PutProfiloDto
import com.iasdietideals24.dietideals24.utilities.services.CompratoreService

class CompratoreRepository(private val service: CompratoreService) {

    suspend fun accountFacebookCompratore(facebookId: String): CompratoreDto {
        return service.accountFacebookCompratore(facebookId).body() ?: CompratoreDto()
    }

    suspend fun accediCompratore(accountEmail: String, accountPassword: String): CompratoreDto {
        return service.accediCompratore(accountEmail, accountPassword).body()
            ?: CompratoreDto()
    }

    suspend fun creazioneAccountCompratore(
        accountEmail: String,
        account: PutProfiloDto
    ): PutProfiloDto {
        return service.creazioneAccountCompratore(accountEmail, account).body()
            ?: PutProfiloDto()
    }

    suspend fun caricaAccountCompratore(accountEmail: String): CompratoreDto {
        return service.caricaAccountCompratore(accountEmail).body() ?: CompratoreDto()
    }
}