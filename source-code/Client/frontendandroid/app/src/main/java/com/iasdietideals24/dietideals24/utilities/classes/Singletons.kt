package com.iasdietideals24.dietideals24.utilities.classes

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.iasdietideals24.dietideals24.utilities.interfaces.API
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

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
    private var _tipoAccount: TipoAccount = TipoAccount.OSPITE

    var id: String
        get() = _id
        set(value) {
            _id = value
        }

    var tipoAccount: TipoAccount
        get() = _tipoAccount
        set(value) {
            _tipoAccount = value
        }
}

enum class TipoAccount { COMPRATORE, VENDITORE, OSPITE }
enum class TipoAsta { INVERSA, TEMPO_FISSO, SILENZIOSA }

object Logger {
    private val LOG_FILE_NAME = "app_log_${Date()}.txt"

    fun log(message: String) {
        writeLogToFile(message)
    }

    private fun writeLogToFile(message: String) {
        try {
            val logFile = File(LOG_FILE_NAME)
            if (!logFile.exists()) {
                logFile.createNewFile()
            }

            val writer = FileWriter(logFile, true)
            writer.write("$currentTime - $message\n")
            writer.close()
        } catch (_: IOException) {
            // Non fare niente
        }
    }

    private val currentTime: String
        @SuppressLint("SimpleDateFormat")
        get() {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return dateFormat.format(Date())
        }
}