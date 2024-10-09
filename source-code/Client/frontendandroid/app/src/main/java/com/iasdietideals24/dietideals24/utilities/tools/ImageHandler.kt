package com.iasdietideals24.dietideals24.utilities.tools

import android.content.Context
import android.net.Uri

class ImageHandler {

    companion object {
        fun encodeImage(uri: Uri?, context: Context): ByteArray? {

            val inputStream = uri?.let { context.contentResolver.openInputStream(it) }
            val bytes = inputStream.use { it?.readBytes() }
            return bytes
        }
    }
}