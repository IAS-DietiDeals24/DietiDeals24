package com.iasdietideals24.dietideals24.utilities.services

import com.iasdietideals24.dietideals24.utilities.dto.ProfiloDto
import com.iasdietideals24.dietideals24.utilities.dto.exceptional.PutProfiloDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfiloService : Service {
    /**
     * Carica tutti i dati di un profilo associato a un account.
     * @param accountEmail Email dell'account del quale recuperare il profilo.
     * @return [ProfiloDto] richiesto. Se non esiste, viene restituito un profilo vuoto.
     */
    @GET("profili")
    suspend fun caricaProfiloDaAccount(
        @Query("email") accountEmail: String
    ): Response<ProfiloDto>

    /**
     * Il metodo recupera il profilo con il nome utente specificato.
     * @param nomeUtente Nome utente del profilo che si vuole recuperare.
     * @return [ProfiloDto] richiesto. Se non esiste, viene restituito un profilo vuoto.
     */
    @GET("profili/{nomeUtente}")
    suspend fun caricaProfilo(
        @Path("nomeUtente") nomeUtente: String,
    ): Response<ProfiloDto>

    /**
     * Aggiorna i dati di un profilo.
     * @param profilo Wrapper con le informazioni necessarie ad aggiornare il profilo.
     * @param nomeUtente Identificativo del profilo da aggiornare.
     * @return Il [ProfiloDto] appena modificato. Se non è stato modificato, viene restituito il profilo originale.
     */
    @PATCH("profili/{nomeUtente}")
    suspend fun aggiornaProfilo(
        @Body profilo: ProfiloDto,
        @Path("nomeUtente") nomeUtente: String
    ): Response<ProfiloDto>

    /**
     * Crea un nuovo account e profilo associato con le credenziali indicate.
     * @param nomeUtente Nome utente del profilo da creare.
     * @param accountProfilo Wrapper con le informazioni necessarie a creare il nuovo account e profilo.
     * @return [PutProfiloDto] appena creato. Se non è stato creato, viene restituito un account vuoto.
     */
    @PUT("profili/{nomeUtente}")
    suspend fun creazioneAccountProfilo(
        @Path("nomeUtente") nomeUtente: String,
        @Body accountProfilo: PutProfiloDto
    ): Response<PutProfiloDto>
}