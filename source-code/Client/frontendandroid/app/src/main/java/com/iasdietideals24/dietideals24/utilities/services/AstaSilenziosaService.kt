package com.iasdietideals24.dietideals24.utilities.services

import com.iasdietideals24.dietideals24.utilities.dto.AstaSilenziosaDto
import com.iasdietideals24.dietideals24.utilities.tools.Page
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AstaSilenziosaService : Service {
    /**
     * Recupera l'elenco di tutte le aste silenziose create dal venditore che ha effettualo l'accesso.
     * @param accountEmail L'email dell'account che ha effettuato l'accesso.
     * @param size Il numero massimo di aste da recuperare.
     * @param page Il numero della pagina da recuperare.
     * @return [Page] di [AstaSilenziosaDto] che contiene un certo numero di aste create. Se non
     * esistono, viene creata una pagina vuota.
     */
    @GET("aste/di-venditori/silenziose")
    suspend fun recuperaAsteCreateSilenziose(
        @Query("email") accountEmail: String,
        @Query("size") size: Long,
        @Query("page") page: Long
    ): Response<Page<AstaSilenziosaDto>>

    /**
     * Carica una nuova asta silenziosa.
     * @param asta Wrapper con le informazioni necessarie a creare l'asta.
     * @return [AstaSilenziosaDto] appena creata. Se non è stata creata, viene restituita un'asta vuota.
     */
    @PUT("aste/di-venditori/silenziose")
    suspend fun creaAstaSilenziosa(
        @Body asta: AstaSilenziosaDto
    ): Response<AstaSilenziosaDto>

    /**
     * Recupera i dettagli dell'asta silenziosa specificata.
     * @param idAsta Identificativo dell'asta da recuperare.
     * @return [AstaSilenziosaDto] richiesta. Se non esiste, viene restituita un'asta vuota.
     */
    @GET("aste/di-venditori/silenziose/{idAsta}")
    suspend fun caricaAstaSilenziosa(
        @Path("idAsta") idAsta: Long
    ): Response<AstaSilenziosaDto>

    /**
     * Elimina un'asta silenziosa e tutti i suoi dati associati dalla base di dati. Manda inoltre
     * una notifica a coloro che hanno partecipato all'asta per avvisare della cancellazione.
     * @param idAsta Identificativo dell'asta da eliminare.
     */
    @DELETE("aste/di-venditori/silenziose/{idAsta}")
    suspend fun eliminaAstaSilenziosa(
        @Path("idAsta") idAsta: Long
    ): Response<Unit>

    /**
     * Aggiorna i dati di un'asta silenziosa.
     * @param asta Wrapper con le informazioni necessarie ad aggiornare l'asta.
     * @param idAsta Identificativo dell'asta da aggiornare.
     * @return L'[AstaSilenziosaDto] appena modificata. Se non è stata modificata, viene restituita l' asta originale.
     */
    @PATCH("aste/di-venditori/silenziose/{idAsta}")
    suspend fun aggiornaAstaSilenziosa(
        @Body asta: AstaSilenziosaDto,
        @Path("idAsta") idAsta: Long
    ): Response<AstaSilenziosaDto>

    /**
     * Recupera l'elenco di tutte le aste silenziose con le quali può interagire il compratore che ha
     * effettuato il login.
     * @param size Il numero massimo di aste da recuperare.
     * @param page Il numero della pagina da recuperare.
     * @return [Page] di [AstaSilenziosaDto] con un certo numero di aste da mostrare nella home.
     * Se non esistono, viene restituita una pagina vuota.
     */
    @GET("aste/di-venditori/silenziose")
    suspend fun recuperaAsteSilenziose(
        @Query("size") size: Long,
        @Query("page") page: Long
    ): Response<Page<AstaSilenziosaDto>>

    /**
     * Recupera l'elenco di tutte le aste silenziose con le quali può interagire l'utente che ha attualmente
     * effettuato il login in base ai filtri di ricerca specificati.
     * Il campo del filtro non sarà mai vuoto; il campo della ricerca potrebbe esserlo. In tal caso,
     * il campo della ricerca viene ignorato e viene restituito l'elenco delle aste che rispettano
     * solo il filtro.
     * Se entrambi i campi sono non vuoti, invece, verranno recuperate le aste che rispettano
     * entrambi i criteri.
     * @param ricerca Stringa di ricerca specificata dall'utente.
     * @param filtro Filtro specificato dall'utente.
     * @param size Il numero massimo di aste da recuperare.
     * @param page Il numero della pagina da recuperare.
     * @return [Page] di [AstaSilenziosaDto] con un certo numero di aste da mostrare nella home dopo
     * la ricerca e/o il filtraggio. Se non esistono, viene restituita una pagina vuota.
     */
    @GET("aste/di-venditori/silenziose")
    suspend fun ricercaAsteSilenziose(
        @Query("ricerca") ricerca: String,
        @Query("filtro") filtro: String,
        @Query("size") size: Long,
        @Query("page") page: Long
    ): Response<Page<AstaSilenziosaDto>>

    /**
     * Recupera l'elenco di tutte le aste silenziose alle quali il compratore ha inviato almeno un'offerta.
     * @param email Identificativo dell'account che ha effettuato l'accesso.
     * @param size Il numero massimo di aste da recuperare.
     * @param page Il numero della pagina da recuperare.
     * @return [Page] di [AstaSilenziosaDto] con un certo numero di aste alle quali l'utente ha
     * partecipato. Se non esistono, viene restituita una pagina vuota.
     */
    @GET("aste/di-venditori/silenziose")
    suspend fun recuperaPartecipazioniSilenziose(
        @Query("email") email: String,
        @Query("size") size: Long,
        @Query("page") page: Long
    ): Response<Page<AstaSilenziosaDto>>
}