package com.iasdietideals24.dietideals24.utilities.services

import com.iasdietideals24.dietideals24.utilities.dto.CategoriaAstaDto
import com.iasdietideals24.dietideals24.utilities.tools.Page
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CategoriaAstaService : Service {
    /**
     * Recupera l'elenco di tutte le categorie di asta esistenti.
     * @return [Page] di [CategoriaAstaDto] che contiene un certo numero di categorie di aste. Se non
     * esistono, viene creata una pagina vuota.
     */
    @GET("categorie-asta")
    suspend fun recuperaCategorieAste(
        @Query("size") size: Long,
        @Query("page") page: Long
    ): Response<Page<CategoriaAstaDto>>
}