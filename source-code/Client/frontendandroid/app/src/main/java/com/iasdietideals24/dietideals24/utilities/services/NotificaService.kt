package com.iasdietideals24.dietideals24.utilities.services

import com.iasdietideals24.dietideals24.utilities.dto.NotificaDto
import com.iasdietideals24.dietideals24.utilities.tools.Page
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificaService : Service {
    /**
     * Recupera tutte le notifiche che sono state indirizzate all'utente che ha effettuato l'accesso.
     * @param idAccount Identificativo dell'account che ha effettuato l'accesso.
     * @param size Il numero massimo di notifiche da recuperare.
     * @param page Il numero della pagina da recuperare.
     * @return [Page] di [NotificaDto] con un certo numero di notifiche da mostrare all'utente.
     * Se non esistono, viene restituita una pagina vuota.
     */
    @GET("notifiche")
    suspend fun recuperaNotifiche(
        @Query("destinatario") idAccount: Long,
        @Query("size") size: Long,
        @Query("page") page: Long
    ): Response<Page<NotificaDto>>
}