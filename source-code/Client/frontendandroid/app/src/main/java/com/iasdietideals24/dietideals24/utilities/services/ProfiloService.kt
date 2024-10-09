package com.iasdietideals24.dietideals24.utilities.services

import com.iasdietideals24.dietideals24.utilities.dto.ProfiloDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
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
     * Il metodo controlla se il nome utente è già associata a un profilo.
     * @param nomeUtente Nome utente del profilo che sta tentando di registrarsi.
     * @return Un [Boolean] che indica se il nome utente è già associato a un profilo.
     */
    @GET("profili/{nomeUtente}/exists")
    suspend fun esisteNomeUtente(
        @Path("nomeUtente") nomeUtente: String,
    ): Response<Boolean>

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
}