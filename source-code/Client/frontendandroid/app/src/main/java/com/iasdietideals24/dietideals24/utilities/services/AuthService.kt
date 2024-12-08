package com.iasdietideals24.dietideals24.utilities.services

import com.iasdietideals24.dietideals24.utilities.dto.utilities.NewTokenDto
import com.iasdietideals24.dietideals24.utilities.dto.utilities.RefreshTokenDto
import com.iasdietideals24.dietideals24.utilities.dto.utilities.UrlDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthService : Service {
    /**
     * Recupera l'URL per effettuare l'autenticazione.
     * @param redirectUri URI al quale si dovrà essere reindirizzato l'utente dopo l'autenticazione.
     * @return [UrlDto] contenente URL per l'autenticazione.
     */
    @GET("auth/url")
    suspend fun recuperaUrlAutenticazione(
        @Query("redirect_uri") redirectUri: String
    ): Response<UrlDto>

    /**
     * Recupera i JWT per autenticare le chiamate API.
     * @param code Codice ottenuto dall'autenticazione.
     * @param redirectUri URI al quale si dovrà essere reindirizzato l'utente dopo l'autenticazione.
     * @return [NewTokenDto] contenente access token, refresh token e scadenza.
     */
    @GET("auth/callback")
    suspend fun recuperaToken(
        @Query("code") code: String,
        @Query("redirect_uri") redirectUri: String
    ): Response<NewTokenDto>

    /**
     * Ottiene un nuovo access token utilizzando il refresh token fornito.
     * @param refreshToken Refresh token per l'autenticazione.
     * @return [NewTokenDto] contenente access token e scadenza.
     */
    @GET("auth/refresh")
    fun aggiornaAccessToken(
        @Query("refresh_token") refreshToken: String
    ): Call<RefreshTokenDto>

    /**
     * Invalida il refresh token fornito.
     * @param refreshToken Refresh token per l'autenticazione.
     * @param redirectUri URI al quale si dovrà essere reindirizzato l'utente dopo l'autenticazione.
     * @param logoutUri URI al quale si dovrà essere reindirizzato l'utente dopo il logout.
     */
    @GET("auth/logout")
    suspend fun logout(
        @Query("refresh_token") refreshToken: String,
        @Query("redirect_uri") redirectUri: String,
        @Query("logout_uri") logoutUri: String
    ): Response<UrlDto>
}