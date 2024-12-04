package com.iasdietideals24.dietideals24.utilities.services

import com.iasdietideals24.dietideals24.utilities.dto.utilities.TokenDto
import com.iasdietideals24.dietideals24.utilities.dto.utilities.UrlDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthService : Service {
    /**
     * Recupera l'URL per effettuare l'autenticazione.
     * @param redirectUri URI al quale si dovrà essere reindirizzato l'utente dopo l'autenticazione.
     * @return [String] contenente URL per l'autenticazione.
     */
    @GET("auth/url")
    suspend fun recuperaUrlAutenticazione(
        @Query("redirect_uri") redirectUri: String
    ): Response<UrlDto>

    /**
     * Recupera il JWT per autenticare le chiamate API.
     * @param code Codice ottenuto dall'autenticazione.
     * @param redirectUri URI al quale si dovrà essere reindirizzato l'utente dopo l'autenticazione.
     * @return [String] contenente il JWT per l'autenticazione delle chiamate API.
     */
    @GET("auth/callback")
    suspend fun recuperaJWT(
        @Query("code") code: String,
        @Query("redirect_uri") redirectUri: String
    ): Response<TokenDto>
}