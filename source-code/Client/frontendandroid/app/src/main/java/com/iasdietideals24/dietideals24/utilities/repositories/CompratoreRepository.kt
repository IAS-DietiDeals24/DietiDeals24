package com.iasdietideals24.dietideals24.utilities.repositories

import com.iasdietideals24.dietideals24.utilities.dto.CompratoreDto
import com.iasdietideals24.dietideals24.utilities.services.CompratoreService

class CompratoreRepository(private val service: CompratoreService) {

    suspend fun accountFacebookCompratore(idFacebook: String): CompratoreDto {
        return service.accountFacebookCompratore(idFacebook).body() ?: CompratoreDto()
    }

    suspend fun accediCompratore(accountEmail: String, accountPassword: String): CompratoreDto {
        return service.accediCompratore(accountEmail, accountPassword).body()
            ?: CompratoreDto()
    }

    suspend fun caricaAccountCompratore(idAccount: Long): CompratoreDto {
        return service.caricaAccountCompratore(idAccount).body() ?: CompratoreDto()
    }

    suspend fun creaAccountCompratore(account: CompratoreDto): CompratoreDto {
        return service.creaAccountCompratore(account).body() ?: CompratoreDto()
    }
}