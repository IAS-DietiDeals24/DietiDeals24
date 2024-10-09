package com.iasdietideals24.dietideals24.utilities.services

import com.iasdietideals24.dietideals24.utilities.dto.OffertaTempoFissoDto
import com.iasdietideals24.dietideals24.utilities.tools.Page
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OffertaTempoFissoService : Service {
    /**
     * Recupera l'offerta più alta inviata a un'asta a tempo fisso.
     * @param idAsta Identificativo dell'asta della quale recuperare l'offerta.
     * @return [OffertaTempoFissoDto] con il valore più alto. Se non esiste, viene restituita un'offerta vuota.
     */
    @GET("offerte/di-venditori/tempo-fisso/findMax")
    suspend fun recuperaOffertaPiuAlta(
        @Query("idAsta") idAsta: Long
    ): Response<OffertaTempoFissoDto>

    /**
     * Recupera l'offerta più alta inviata a un'asta a tempo fisso dall'utente.
     * @param idAsta Identificativo dell'asta della quale recuperare l'offerta.
     * @param accountEmail Email dell'account dell'utente.
     * @return [OffertaTempoFissoDto] con il valore più alto. Se non esiste, viene restituita un'offerta vuota.
     */
    @GET("offerte/di-venditori/tempo-fisso/findMax")
    suspend fun recuperaOffertaPersonalePiuAltaTempoFisso(
        @Query("idAsta") idAsta: Long,
        @Query("email") accountEmail: String
    ): Response<OffertaTempoFissoDto>

    /**
     * Invia un'offerta a un'asta a tempo fisso. Manda inoltre una notifica al proprietario
     * dell'asta con le informazioni dell'offerta.
     * @param offerta Wrapper con le informazioni necessarie a inviare l'offerta.
     * @param idAsta Identificativo dell'asta alla quale inviare l'offerta.
     * @return [OffertaTempoFissoDto] appena inviata. Se non è stata inviata, viene restituita un'offerta vuota.
     */
    @POST("offerte/di-venditori/tempo-fisso")
    suspend fun inviaOffertaTempoFisso(
        @Body offerta: OffertaTempoFissoDto,
        @Query("idAsta") idAsta: Long
    ): Response<OffertaTempoFissoDto>

    /**
     * Recupera tutte le offerte inviate a un'asta a tempo fisso.
     * @param idAsta Identificativo dell'asta della quale recuperare le offerte.
     * @param size Il numero massimo di aste da recuperare.
     * @param page Il numero della pagina da recuperare.
     * @return [Page] di [OffertaTempoFissoDto] con un certo numero di offerte dell'asta. Se non
     * esistono, viene restituita una pagina vuota.
     */
    @GET("offerte/di-venditori/tempo-fisso")
    suspend fun recuperaOfferteTempoFisso(
        @Query("idAsta") idAsta: Long,
        @Query("size") size: Long,
        @Query("page") page: Long
    ): Response<Page<OffertaTempoFissoDto>>

}