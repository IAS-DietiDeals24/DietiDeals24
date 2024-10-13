package com.iasdietideals24.dietideals24.utilities.tools

import android.annotation.SuppressLint
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

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