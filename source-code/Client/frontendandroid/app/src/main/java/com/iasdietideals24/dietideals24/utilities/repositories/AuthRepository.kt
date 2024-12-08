package com.iasdietideals24.dietideals24.utilities.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.iasdietideals24.dietideals24.utilities.dto.utilities.NewTokenDto
import com.iasdietideals24.dietideals24.utilities.dto.utilities.RefreshTokenDto
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.services.AuthService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.Response

class AuthRepository(
    private val service: AuthService,
    private val dataStore: DataStore<Preferences>
) {
    suspend fun recuperaUrlAutenticazione(redirectUri: String): String {
        return service.recuperaUrlAutenticazione(redirectUri).body()?.url ?: ""
    }

    suspend fun recuperaToken(code: String, redirectUri: String): NewTokenDto {
        return service.recuperaToken(code, redirectUri).body() ?: NewTokenDto()
    }

    fun aggiornaAccessToken(refreshToken: String): Response<RefreshTokenDto> {
        return service.aggiornaAccessToken(refreshToken).execute()
    }

    suspend fun logout(refreshToken: String, redirectUri: String, logoutUri: String): String {
        return service.logout(refreshToken, redirectUri, logoutUri).body()?.url ?: ""
    }

    suspend fun scriviRefreshToken(jwt: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("RefreshToken")] = jwt
        }
    }

    suspend fun scriviRefreshToken(): String {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey("RefreshToken")] ?: ""
        }.first()
    }

    suspend fun cancellaRefreshToken() {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey("RefreshToken"))
        }
    }

    suspend fun scriviRuolo(ruolo: TipoAccount) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("Type")] = ruolo.name
        }
    }

    suspend fun leggiRuolo(): TipoAccount {
        return dataStore.data.map { preferences ->
            TipoAccount.valueOf(preferences[stringPreferencesKey("Type")] ?: "OSPITE")
        }.first()
    }

    suspend fun cancellaRuolo() {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey("Type"))
        }
    }
}