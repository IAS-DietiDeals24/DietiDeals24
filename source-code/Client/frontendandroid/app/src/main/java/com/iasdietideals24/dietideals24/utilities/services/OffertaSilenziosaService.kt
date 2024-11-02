package com.iasdietideals24.dietideals24.utilities.services

import com.iasdietideals24.dietideals24.utilities.dto.OffertaSilenziosaDto
import com.iasdietideals24.dietideals24.utilities.tools.Page
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OffertaSilenziosaService : Service {
    /**
     * Recupera l'offerta più alta inviata a un'asta silenziosa dall'utente.
     * @param idAsta Identificativo dell'asta della quale recuperare l'offerta.
     * @param accountEmail Email dell'account dell'utente.
     * @return [OffertaSilenziosaDto] con il valore più alto. Se non esiste, viene restituita un'offerta vuota.
     */
    @GET("offerte/di-venditori/silenziose/findMax")
    suspend fun recuperaOffertaPersonalePiuAltaSilenziosa(
        @Query("idAsta") idAsta: Long,
        @Query("email") accountEmail: String
    ): Response<OffertaSilenziosaDto>

    /**
     * Invia un'offerta a un'asta silenziosa. Manda inoltre una notifica al proprietario dell'asta
     * con le informazioni dell'offerta.
     * @param offerta Wrapper con le informazioni necessarie a inviare l'offerta.
     * @param idAsta Identificativo dell'asta alla quale inviare l'offerta.
     * @return [OffertaSilenziosaDto] appena inviata. Se non è stata inviata, viene restituita un'offerta vuota.
     */
    @PUT("offerte/di-venditori/silenziose")
    suspend fun inviaOffertaSilenziosa(
        @Body offerta: OffertaSilenziosaDto,
        @Query("idAsta") idAsta: Long
    ): Response<OffertaSilenziosaDto>

    /**
     * Recupera tutte le offerte inviate a un'asta silenziosa.
     * @param idAsta Identificativo dell'asta della quale recuperare le offerte.
     * @param size Il numero massimo di aste da recuperare.
     * @param page Il numero della pagina da recuperare.
     * @return [Page] di [OffertaSilenziosaDto] con un certo numero di offerte dell'asta. Se non
     * esistono, viene restituita una pagina vuota.
     */
    @GET("offerte/di-venditori/silenziose")
    suspend fun recuperaOfferteSilenziose(
        @Query("idAsta") idAsta: Long,
        @Query("size") size: Long,
        @Query("page") page: Long
    ): Response<Page<OffertaSilenziosaDto>>

    /**
     * Accetta un'offerta di un'asta silenziosa e, nella stessa transazione, rifiuta tutte le altre
     * offerte per la stessa asta. Manda inoltre una notifica a chi ha avanzato le offerte per
     * avvisare dell'esito.
     * @param idOfferta Identificativo dell'offerta da accettare.
     * @return Un [Boolean] che indica se l'operazione la transazione ha avuto successo.
     */
    @PATCH("offerte/di-venditori/silenziose/{idOfferta}")
    suspend fun accettaOfferta(
        @Body offerta: OffertaSilenziosaDto,
        @Path("idOfferta") idOfferta: Long
    ): Response<Boolean>

    /**
     * Rifiuta un'offerta di un'asta silenziosa. Manda inoltre una notifica a chi ha avanzato l'offerta
     * per avvisare dell'esito.
     * @param idOfferta Identificativo dell'offerta da accettare.
     * @return Un [Boolean] che indica se l'operazione la transazione ha avuto successo.
     */
    @PATCH("offerte/di-venditori/silenziose/{idOfferta }")
    suspend fun rifiutaOfferta(
        @Body offerta: OffertaSilenziosaDto,
        @Path("idOfferta") idOfferta: Long
    ): Response<Boolean>
}