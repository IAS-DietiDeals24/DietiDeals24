package com.iasdietideals24.dietideals24.utilities.services

import com.iasdietideals24.dietideals24.utilities.dto.VenditoreDto
import com.iasdietideals24.dietideals24.utilities.dto.exceptional.PutProfiloDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VenditoreService : Service {
    /**
     * Il metodo recupera l'account associato all'ID dell'account Facebook, ma solo se l'utente ha
     * un account associato a questo profilo Facebook di tipo venditore.
     * @param facebookId Identificativo dell'account Facebook dell'utente.
     * @return [VenditoreDto] associato a questo account Facebook. Se non esiste, viene restituito un
     * account vuoto.
     */
    @GET("accounts/venditori")
    suspend fun accountFacebookVenditore(
        @Query("facebookId") facebookId: String
    ): Response<VenditoreDto>

    /**
     * Il metodo recupera l'account venditore che ha effettuato l'accesso.
     * @param accountEmail Email dell'account che sta tentando di accedere.
     * @param accountPassword Password dell'account che sta tentando di accedere.
     * @return [VenditoreDto] che ha effettuato l'accesso. Se non esiste, viene restituito un account
     * vuoto.
     */
    @GET("accounts/venditori/{email}")
    suspend fun accediVenditore(
        @Path("email") accountEmail: String,
        @Query("password") accountPassword: String
    ): Response<VenditoreDto>

    /**
     * Crea un nuovo account venditore con le credenziali indicate.
     * @param accountEmail L 'email dell'account da creare.
     * @param account Wrapper con le informazioni necessarie a creare il nuovo account.
     * @return [PutProfiloDto] appena creato. Se non è stato creato, viene restituito un account vuoto.
     */
    @POST("accounts/venditori/{email}")
    suspend fun creazioneAccountVenditore(
        @Path("email") accountEmail: String,
        @Body account: PutProfiloDto
    ): Response<PutProfiloDto>

    /**
     * Il metodo recupera l'account venditore specificato.
     * @param accountEmail Email dell'account.
     * @return [VenditoreDto] richiesto. Se non esiste, viene restituito un account vuoto.
     */
    @GET("accounts/venditori/{email}")
    suspend fun caricaAccountVenditore(
        @Path("email") accountEmail: String,
    ): Response<VenditoreDto>
}