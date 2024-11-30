package com.iasdietideals24.dietideals24.utilities.services

import com.iasdietideals24.dietideals24.utilities.dto.CompratoreDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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
    @GET("accounts/compratori/facebook/{idFacebook}") //TODO ancora da implementare su backend
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
    @GET("accounts/compratori/{email}") //TODO ancora da implementare su backend
    suspend fun accediCompratore(
        @Path("email") accountEmail: String,
        @Query("password") accountPassword: String
    ): Response<CompratoreDto>

    /**
     * Il metodo recupera l'account compratore specificato.
     * @param idAccount Id dell'account.
     * @return [CompratoreDto] richiesto. Se non esiste, viene restituito un account vuoto.
     */
    @GET("accounts/compratori/{idAccount}")
    suspend fun caricaAccountCompratore(
        @Path("idAccount") idAccount: Long,
    ): Response<CompratoreDto>

    /**
     * Il metodo crea un account compratore con i dati specificati.
     * @param account Oggetto [CompratoreDto] che contiene i dati necessari alla creazione.
     * @return [CompratoreDto] appena creato. Se non è stato creato, viene restituito un account vuoto.
     */
    @POST("accounts/compratori")
    suspend fun creaAccountCompratore(
        @Body account: CompratoreDto
    ): Response<CompratoreDto>
}