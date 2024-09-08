package com.iasdietideals24.dietideals24.utilities.classes

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.iasdietideals24.dietideals24.utilities.interfaces.API
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitController {
    private const val API_URL = "http://localhost:8080"

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

object APIController {
    val instance: API by lazy {
        RetrofitController.instance.create(API::class.java)
    }
}

object DataStore {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dietideals24")
}

object CurrentUser {
    private var _id: String = ""

    var id: String
        get() = _id
        set(value) {
            _id = value
        }
}