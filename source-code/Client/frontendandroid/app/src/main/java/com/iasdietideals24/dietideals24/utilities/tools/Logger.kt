package com.iasdietideals24.dietideals24.utilities.tools

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey

class Logger(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun scriviLog(string: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("Log")] =
                preferences[stringPreferencesKey("Log")] + "\r\n" + string
        }
    }

    suspend fun cancellaLog() {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey("Log"))
        }
    }
}