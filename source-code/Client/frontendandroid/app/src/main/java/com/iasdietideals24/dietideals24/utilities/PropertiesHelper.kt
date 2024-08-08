package com.iasdietideals24.dietideals24.utilities

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.iasdietideals24.dietideals24.R
import java.io.IOException
import java.util.Properties


class PropertiesHelper {
    companion object {
        private val TAG: String = "PropertiesHelper"

        fun getConfigValue(context: Context, name: String?): String? {
            val resources = context.resources

            try {
                val rawResource = resources.openRawResource(R.raw.config)
                val properties: Properties = Properties()
                properties.load(rawResource)
                return properties.getProperty(name)
            } catch (e: Resources.NotFoundException) {
                Log.e(TAG, "Unable to find the config file: " + e.message)
            } catch (e: IOException) {
                Log.e(TAG, "Failed to open config file.")
            }

            return null
        }
    }
}