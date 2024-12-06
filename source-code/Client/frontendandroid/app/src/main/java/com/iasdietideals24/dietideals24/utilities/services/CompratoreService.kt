package com.iasdietideals24.dietideals24.utilities.services

import com.iasdietideals24.dietideals24.utilities.dto.CompratoreDto
import com.iasdietideals24.dietideals24.utilities.tools.Page
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CompratoreService : Service {
    /**
     * Il metodo recupera l'account compratore che ha effettuato l'accesso.
     * @param accountEmail Email dell'account che sta tentando di accedere.
     * @return [CompratoreDto] che ha effettuato l'accesso. Se non esiste, viene restituito un account
     * vuoto.
     */
    @GET("accounts/compratori")
    suspend fun accediCompratore(
        @Query("email") accountEmail: String,
    ): Response<Page<CompratoreDto>>

    /**
     * Il metodo recupera l'account compratore specificato.
     * @param idAccount Id dell'account.
     * @return [CompratoreDto] richiesto. Se non esiste, viene restituito un account vuoto.
     */
    @GET("accounts/compratori/{idAccount}")
    suspend fun caricaAccountCompratore(
        @Path("idAccount") idAccount: Long,
    ): Response<CompratoreDto>
}