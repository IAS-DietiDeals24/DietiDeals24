package com.iasdietideals24.dietideals24.utilities.services

import com.iasdietideals24.dietideals24.utilities.dto.AstaTempoFissoDto
import com.iasdietideals24.dietideals24.utilities.tools.Page
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AstaTempoFissoService : Service {
    /**
     * Recupera l'elenco di tutte le aste a tempo fisso create dal venditore che ha effettualo l'accesso.
     * @param accountEmail L'email dell'account che ha effettuato l'accesso.
     * @param size Il numero massimo di aste da recuperare.
     * @param page Il numero della pagina da recuperare.
     * @return [Page] di [AstaTempoFissoDto] che contiene un certo numero di aste create. Se non
     * esistono, viene creata una pagina vuota.
     */
    @GET("aste/di-venditori/tempo-fisso")
    suspend fun recuperaAsteCreateTempoFisso(
        @Query("email") accountEmail: String,
        @Query("size") size: Long,
        @Query("page") page: Long
    ): Response<Page<AstaTempoFissoDto>>

    /**
     * Carica una nuova asta a tempo fisso.
     * @param asta Wrapper con le informazioni necessarie a creare l'asta.
     * @return [AstaTempoFissoDto] appena creata. Se non è stata creata, viene restituita un'asta vuota.
     */
    @POST("aste/di-venditori/tempo-fisso")
    suspend fun creaAstaTempoFisso(
        @Body asta: AstaTempoFissoDto
    ): Response<AstaTempoFissoDto>

    /**
     * Recupera i dettagli dell'asta a tempo fisso specificata.
     * @param idAsta Identificativo dell'asta da recuperare.
     * @return [AstaTempoFissoDto] richiesta. Se non esiste, viene restituita un'asta vuota.
     */
    @GET("aste/di-venditori/tempo-fisso/{idAsta}")
    suspend fun caricaAstaTempoFisso(
        @Path("idAsta") idAsta: Long
    ): Response<AstaTempoFissoDto>

    /**
     * Elimina un'asta a tempo fisso e tutti i suoi dati associati dalla base di dati. Manda inoltre
     * una notifica a coloro che hanno partecipato all'asta per avvisare della cancellazione.
     * @param idAsta Identificativo dell'asta da eliminare.
     */
    @DELETE("aste/di-venditori/tempo-fisso/{idAsta}")
    suspend fun eliminaAstaTempoFisso(
        @Path("idAsta") idAsta: Long
    ): Response<Unit>

    /**
     * Aggiorna i dati di un'asta a tempo fisso.
     * @param asta Wrapper con le informazioni necessarie ad aggiornare l'asta.
     * @param idAsta Identificativo dell'asta da aggiornare.
     * @return L'[AstaTempoFissoDto] appena modificata. Se non è stata modificata, viene restituita l' asta originale.
     */
    @PATCH("aste/di-venditori/tempo-fisso/{idAsta}")
    suspend fun aggiornaAstaTempoFisso(
        @Body asta: AstaTempoFissoDto,
        @Path("idAsta") idAsta: Long
    ): Response<AstaTempoFissoDto>

    /**
     * Recupera l'elenco di tutte le aste a tempo fisso con le quali può interagire il compratore che ha
     * effettuato il login.
     * @param size Il numero massimo di aste da recuperare.
     * @param page Il numero della pagina da recuperare.
     * @return [Page] di [AstaTempoFissoDto] che contiene un certo numero di aste create. Se non
     * esistono, viene restituita una pagina vuota.
     */
    @GET("aste/di-venditori/tempo-fisso")
    suspend fun recuperaAsteTempoFisso(
        @Query("size") size: Long,
        @Query("page") page: Long
    ): Response<Page<AstaTempoFissoDto>>

    /**
     * Recupera l'elenco di tutte le aste a tempo fisso con le quali può interagire l'utente che ha attualmente
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
     * @return [Page] di [AstaTempoFissoDto] con un certo numero di aste da mostrare nella home dopo
     * la ricerca e/o il filtraggio. Se non esistono, viene restituita una pagina vuota.
     */
    @GET("aste/di-venditori/tempo-fisso")
    suspend fun ricercaAsteTempoFisso(
        @Query("ricerca") ricerca: String,
        @Query("filtro") filtro: String,
        @Query("size") size: Long,
        @Query("page") page: Long
    ): Response<Page<AstaTempoFissoDto>>

    /**
     * Recupera l'elenco di tutte le aste a tempo fisso alle quali il compratore ha inviato almeno un'offerta.
     * @param email Identificativo dell'account che ha eff ettuato l'accesso.
     * @param size Il numero massimo di aste da recuperare.
     * @param page Il numero della pagina da recuperare.
     * @return [Page] di [AstaTempoFissoDto] con un certo numero di aste alle quali l'utente ha
     * partecipato. Se non esistono, viene restituita una pagina vuota.
     */
    @GET("aste/di-venditori/tempo-fisso")
    suspend fun recuperaPartecipazioniTempoFisso(
        @Query("email") email: String,
        @Query("size") size: Long,
        @Query("page") page: Long
    ): Response<Page<AstaTempoFissoDto>>
}