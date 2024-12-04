package com.iasdietideals24.dietideals24.utilities.services

import com.iasdietideals24.dietideals24.utilities.dto.AstaInversaDto
import com.iasdietideals24.dietideals24.utilities.tools.Page
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AstaInversaService : Service {
    /**
     * Recupera l'elenco di tutte le aste inverse create dal compratore che ha effettualo l'accesso.
     * @param idAccount Id dell'account che ha effettuato l'accesso.
     * @param size Il numero massimo di aste da recuperare.
     * @param page Il numero della pagina da recuperare.
     * @return [Page] di [AstaInversaDto] che contiene un certo numero di aste create. Se non
     * esistono, viene creata una pagina vuota.
     */
    @GET("aste/di-compratori/inverse")
    suspend fun recuperaAsteCreateInverse(
        @Query("proprietario") idAccount: Long,
        @Query("size") size: Long,
        @Query("page") page: Long
    ): Response<Page<AstaInversaDto>>

    /**
     * Carica una nuova asta inversa.
     * @param asta Wrapper con le informazioni necessarie a creare l'asta.
     * @return [AstaInversaDto] appena creata. Se non è stata creata, viene restituita un'asta vuota.
     */
    @POST("aste/di-compratori/inverse")
    suspend fun creaAstaInversa(
        @Body asta: AstaInversaDto
    ): Response<AstaInversaDto>

    /**
     * Recupera i dettagli dell'asta inversa specificata.
     * @param idAsta Identificativo dell'asta da recuperare.
     * @return [AstaInversaDto] richiesta. Se non esiste, viene restituita un'asta vuota.
     */
    @GET("aste/di-compratori/inverse/{idAsta}")
    suspend fun caricaAstaInversa(
        @Path("idAsta") idAsta: Long
    ): Response<AstaInversaDto>

    /**
     * Elimina un'asta inversa e tutti i suoi dati associati dalla base di dati. Manda inoltre
     * una notifica a coloro che hanno partecipato all'asta per avvisare della cancellazione.
     * @param idAsta Identificativo dell'asta da eliminare.
     */
    @DELETE("aste/di-compratori/inverse/{idAsta}")
    suspend fun eliminaAstaInversa(
        @Path("idAsta") idAsta: Long
    ): Response<Unit>

    /**
     * Aggiorna i dati di un'asta inversa.
     * @param asta Wrapper con le informazioni necessarie ad aggiornare l'asta.
     * @param idAsta Identificativo dell'asta da aggiornare.
     * @return L'[AstaInversaDto] appena modificata. Se non è stata modificata, viene restituita l' asta originale.
     */
    @PATCH("aste/di-compratori/inverse/{idAsta}")
    suspend fun aggiornaAstaInversa(
        @Body asta: AstaInversaDto,
        @Path("idAsta") idAsta: Long
    ): Response<AstaInversaDto>

    /**
     * Recupera l'elenco di tutte le aste inverse con le quali può interagire il venditore che ha
     * effettuato il login.
     * @param size Il numero massimo di aste da recuperare.
     * @param page Il numero della pagina da recuperare.
     * @return [Page] di [AstaInversaDto] che contiene un certo numero di aste create. Se non
     * esistono, viene restituita una pagina vuota.
     */
    @GET("aste/di-compratori/inverse")
    suspend fun recuperaAsteInverse(
        @Query("size") size: Long,
        @Query("page") page: Long
    ): Response<Page<AstaInversaDto>>

    /**
     * Recupera l'elenco di tutte le aste inverse con le quali può interagire l'utente che ha attualmente
     * effettuato il login in base ai filtri di ricerca specificati.
     * Il campo del filtro non sarà mai vuoto; il campo della ricerca potrebbe esserlo. In tal caso,
     * il campo della ricerca viene ignorato e viene restituito l'elenco delle aste che rispettano
     * solo il filtro.
     * Se entrambi i campi sono non vuoti, invece, verranno recuperate le aste che rispettano
     * entrambi i criteri.
     * @param nome Stringa di ricerca specificata dall'utente.
     * @param categoria Filtro specificato dall'utente.
     * @param size Il numero massimo di aste da recuperare.
     * @param page Il numero della pagina da recuperare.
     * @return [Page] di [AstaInversaDto] con un certo numero di aste da mostrare nella home dopo
     * la ricerca e/o il filtraggio. Se non esistono, viene restituita una pagina vuota.
     */
    @GET("aste/di-compratori/inverse")
    suspend fun ricercaAsteInverse(
        @Query("nome") nome: String,
        @Query("categoria") categoria: String,
        @Query("size") size: Long,
        @Query("page") page: Long
    ): Response<Page<AstaInversaDto>>

    /**
     * Recupera l'elenco di tutte le aste inverse alle quali il venditore ha inviato almeno un'offerta.
     * @param idAccount Identificativo dell'account che ha effettuato l'accesso.
     * @param size Il numero massimo di aste da recuperare.
     * @param page Il numero della pagina da recuperare.
     * @return [Page] di [AstaInversaDto] con un certo numero di aste alle quali l'utente ha
     * partecipato. Se non esistono, viene restituita una pagina vuota.
     */
    @GET("aste/di-compratori/inverse")
    suspend fun recuperaPartecipazioniInverse(
        @Query("offerente") idAccount: Long,
        @Query("size") size: Long,
        @Query("page") page: Long
    ): Response<Page<AstaInversaDto>>
}