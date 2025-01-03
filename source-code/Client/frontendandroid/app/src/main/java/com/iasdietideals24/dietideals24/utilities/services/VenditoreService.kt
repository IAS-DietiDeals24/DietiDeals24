package com.iasdietideals24.dietideals24.utilities.services

import com.iasdietideals24.dietideals24.utilities.dto.VenditoreDto
import com.iasdietideals24.dietideals24.utilities.tools.Page
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VenditoreService : Service {
    /**
     * Il metodo recupera l'account venditore che ha effettuato l'accesso.
     * @param accountEmail Email dell'account che sta tentando di accedere.
     * @return [VenditoreDto] che ha effettuato l'accesso. Se non esiste, viene restituito un account
     * vuoto.
     */
    @GET("accounts/venditori")
    suspend fun accediVenditore(
        @Query("email") accountEmail: String,
    ): Response<Page<VenditoreDto>>

    /**
     * Il metodo recupera l'account venditore specificato.
     * @param idAccount Id dell'account.
     * @return [VenditoreDto] richiesto. Se non esiste, viene restituito un account vuoto.
     */
    @GET("accounts/venditori/{idAccount}")
    suspend fun caricaAccountVenditore(
        @Path("idAccount") idAccount: Long,
    ): Response<VenditoreDto>

    /**
     * Il metodo crea un account venditore con i dati specificati.
     * @param account Oggetto [VenditoreDto] che contiene i dati necessari alla creazione.
     * @return [VenditoreDto] appena creato. Se non è stato creato, viene restituito un account vuoto.
     */
    @POST("accounts/venditori")
    suspend fun creaAccountVenditore(
        @Body account: VenditoreDto
    ): Response<VenditoreDto>
}