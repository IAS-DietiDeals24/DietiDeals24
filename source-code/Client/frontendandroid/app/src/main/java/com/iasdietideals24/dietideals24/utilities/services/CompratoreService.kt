package com.iasdietideals24.dietideals24.utilities.services

import com.iasdietideals24.dietideals24.utilities.dto.CompratoreDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CompratoreService : Service {
    /**
     * Il metodo recupera l'account associato all'ID dell'account Facebook, ma solo se l'utente ha
     * un account associato a questo profilo Facebook di tipo compratore.
     * @param idFacebook Identificativo dell'account Facebook dell'utente.
     * @return [CompratoreDto] associato a questo account Facebook. Se non esiste, viene restituito un
     * account vuoto.
     */
    @GET("accounts/compratori/facebook/{idFacebook}")
    suspend fun accountFacebookCompratore(
        @Path("idFacebook") idFacebook: String
    ): Response<CompratoreDto>

    /**
     * Il metodo recupera l'account compratore che ha effettuato l'accesso.
     * @param accountEmail Email dell'account che sta tentando di accedere.
     * @param accountPassword Password dell'account che sta tentando di accedere.
     * @return [CompratoreDto] che ha effettuato l'accesso. Se non esiste, viene restituito un account
     * vuoto.
     */
    @GET("accounts/compratori/{email}")
    suspend fun accediCompratore(
        @Path("email") accountEmail: String,
        @Query("password") accountPassword: String
    ): Response<CompratoreDto>

    /**
     * Il metodo recupera l'account compratore specificato.
     * @param accountEmail Email dell'account.
     * @return [CompratoreDto] richiesto. Se non esiste, viene restituito un account vuoto.
     */
    @GET("accounts/compratori/{email}")
    suspend fun caricaAccountCompratore(
        @Path("email") accountEmail: String,
    ): Response<CompratoreDto>

    /**
     * Il metodo crea un account compratore con i dati specificati.
     * @param accountEmail Email dell'account.
     * @param account Oggetto [CompratoreDto] che contiene i dati necessari alla creazione.
     * @return [CompratoreDto] appena creato. Se non è stato creato, viene restituito un account vuoto.
     */
    @PUT("accounts/compratori/{email}")
    suspend fun creaAccountCompratore(
        @Path("email") accountEmail: String,
        @Body account: CompratoreDto
    ): Response<CompratoreDto>
}