package com.iasdietideals24.dietideals24.utilities.services

import com.iasdietideals24.dietideals24.utilities.dto.OffertaInversaDto
import com.iasdietideals24.dietideals24.utilities.tools.Page
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface OffertaInversaService : Service {
    /**
     * Recupera l'offerta più bassa inviata a un'asta inversa.
     * @param idAsta Identificativo dell'asta della quale recuperare l'offerta.
     * @return [OffertaInversaDto] con il valore più basso. Se non esiste, viene restituita un'offerta vuota.
     */
    @GET("offerte/di-compratori/inverse/findMin")
    suspend fun recuperaOffertaPiuBassa(
        @Query("idAsta") idAsta: Long
    ): Response<OffertaInversaDto>

    /**
     * Recupera l'offerta più bassa inviata a un'asta inversa da un utente.
     * @param idAsta Identificativo dell'asta della quale recuperare l'offerta.
     * @param accountEmail Email dell'account dell'utente.
     * @return [OffertaInversaDto] con il valore più basso. Se non esiste, viene restituita un'offerta vuota.
     */
    @GET("offerte/di-compratori/inverse/findMin")
    suspend fun recuperaOffertaPersonalePiuBassaInversa(
        @Query("idAsta") idAsta: Long,
        @Query("email") accountEmail: String
    ): Response<OffertaInversaDto>

    /**
     * Invia un'offerta a un'asta inversa. Manda inoltre una notifica al proprietario dell'asta con
     * le informazioni dell'offerta.
     * @param offerta Wrapper con le informazioni necessarie a inviare l'offerta.
     * @param idAsta Identificativo dell'asta alla quale inviare l'offerta.
     * @return [OffertaInversaDto] appena inviata. Se non è stata inviata, viene restituita un'offerta vuota.
     */
    @PUT("offerte/di-compratori/inverse")
    suspend fun inviaOffertaInversa(
        @Body offerta: OffertaInversaDto,
        @Query("idAsta") idAsta: Long
    ): Response<OffertaInversaDto>

    /**
     * Recupera tutte le offerte inviate a un'asta inversa.
     * @param idAsta Identificativo dell'asta della quale recuperare le offerte.
     * @param size Il numero massimo di aste da recuperare.
     * @param page Il numero della pagina da recuperare.
     * @return [Page] di [OffertaInversaDto] con un certo numero di offerte dell'asta. Se non
     * esistono, viene restituita una pagina vuota.
     */
    @GET("offerte/di-compratori/inverse")
    suspend fun recuperaOfferteInverse(
        @Query("idAsta") idAsta: Long,
        @Query("size") size: Long,
        @Query("page") page: Long
    ): Response<Page<OffertaInversaDto>>
}