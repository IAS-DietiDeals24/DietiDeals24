package com.iasdietideals24.dietideals24.utilities.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.services.AuthService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AuthRepository(
    private val service: AuthService,
    private val dataStore: DataStore<Preferences>
) {
    suspend fun recuperaUrlAutenticazione(redirectUri: String): String {
        return service.recuperaUrlAutenticazione(redirectUri).body()?.url ?: ""
    }

    suspend fun recuperaJWT(code: String, redirectUri: String): String {
        return service.recuperaJWT(code, redirectUri).body()?.token ?: ""
    }

    suspend fun scriviJWT(jwt: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("JWT")] = jwt
        }
    }

    suspend fun leggiJWT(): String {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey("JWT")] ?: ""
        }.first()
    }

    suspend fun cancellaJWT() {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey("JWT"))
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