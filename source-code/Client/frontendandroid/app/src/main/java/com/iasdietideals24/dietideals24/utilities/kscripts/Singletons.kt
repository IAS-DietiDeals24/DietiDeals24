package com.iasdietideals24.dietideals24.utilities.kscripts

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.services.Service
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

object RetrofitController {
    inline fun <reified T : Service> service(): T {
        return Retrofit.Builder()
            .baseUrl("http://localhost:8080")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
            .create(T::class.java)
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