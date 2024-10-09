package com.iasdietideals24.dietideals24.utilities.services

import com.iasdietideals24.dietideals24.utilities.dto.CompratoreDto
import com.iasdietideals24.dietideals24.utilities.dto.exceptional.PutProfiloDto
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
     * @param facebookId Identificativo dell'account Facebook dell'utente.
     * @return [CompratoreDto] associato a questo account Facebook. Se non esiste, viene restituito un
     * account vuoto.
     */
    @GET("accounts/compratori")
    suspend fun accountFacebookCompratore(
        @Query("facebookId") facebookId: String
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
     * Crea un nuovo account compratore con le credenziali indicate.
     * @param accountEmail L'email dell'account da creare.
     * @param account Wrapper con le informazioni necessarie a creare il nuovo account.
     * @return [PutProfiloDto] appena creato. Se non è stato creato, viene restituito un account vuoto.
     */
    @POST("accounts/compratori/{email}")
    suspend fun creazioneAccountCompratore(
        @Path("email") accountEmail: String,
        @Body account: PutProfiloDto
    ): Response<PutProfiloDto>

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
     * Il metodo controlla se l'email è già associata a un account compratore.
     * @param accountEmail Email dell'account che sta tentando di registrarsi.
     * @return Un [Boolean] che indica se l'email è già associata a un account dello stesso tipo.
     */
    @GET("accounts/compratori/{email}/exists")
    suspend fun esisteEmailCompratore(
        @Path("email") accountEmail: String,
    ): Response<Boolean>

    /**
     * Il metodo controlla se l'email che si sta usando per creare l'account compratore è già associato
     * a un account di tipo venditore.
     * @param accountEmail Email dell'account che sta tentando di registrarsi.
     * @return Un [Boolean] che indica se l'email è già associata a un account di tipo diverso.
     */
    @GET("accounts/compratori/{email}")
    suspend fun associaCreaProfiloCompratore(
        @Path("email") accountEmail: String,
    ): Response<CompratoreDto>
}