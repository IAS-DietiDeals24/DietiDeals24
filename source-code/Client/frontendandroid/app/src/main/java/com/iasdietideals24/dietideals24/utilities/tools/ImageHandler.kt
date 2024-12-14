package com.iasdietideals24.dietideals24.utilities.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream

class ImageHandler {

    companion object {
        fun comprimiByteArray(
            uri: Uri?,
            context: Context,
            maxWidth: Int = 1024,
            maxHeight: Int = 1024,
            quality: Int = 75
        ): ByteArray {
            // Se l'uri è nullo, restituisci un array vuoto
            if (uri == null) return ByteArray(0)

            // Apri uno stream di input dall'URI
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: throw IllegalArgumentException("Impossibile aprire lo stream di input")

            // Leggi l'orientamento dai metadati EXIF
            val exif = try {
                inputStream.use {
                    context.contentResolver.openInputStream(uri)?.let {
                        ExifInterface(it)
                    }
                }
            } catch (e: Exception) {
                Log.e("comprimiByteArray", "Errore lettura EXIF", e)
                null
            }

            // Decodifica il bitmap con un'opzione che riduca l'utilizzo di memoria
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }

            // Riapri lo stream di input
            val boundInputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(boundInputStream, null, options)

            // Calcola il ridimensionamento mantenendo l'aspect ratio
            val (newWidth, newHeight) = calcolaNuoveDimensioniPreservandoAspectRatio(
                options.outWidth,
                options.outHeight,
                maxWidth,
                maxHeight
            )

            // Calcola inSampleSize per ridurre l'uso della memoria
            options.inSampleSize = calcolaDimensioniCampionamento(options, newWidth, newHeight)
            options.inJustDecodeBounds = false

            // Riapri lo stream di input
            val decodeInputStream = context.contentResolver.openInputStream(uri)

            // Decodifica il bitmap
            val bitmap = BitmapFactory.decodeStream(decodeInputStream, null, options)
                ?: throw IllegalArgumentException("Impossibile decodificare Bitmap")

            // Ridimensiona il bitmap mantenendo l'aspect ratio
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)

            // Ruota il bitmap basandosi sui metadati EXIF
            val rotatedBitmap =
                exif?.let { ruotaImmagineSeNecessario(scaledBitmap, it) } ?: scaledBitmap

            // Compress to ByteArray
            return ByteArrayOutputStream().use { outputStream ->
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                outputStream.toByteArray()
            }
        }

        // Calcola le nuove dimensioni preservando l'aspect ratio
        private fun calcolaNuoveDimensioniPreservandoAspectRatio(
            originalWidth: Int,
            originalHeight: Int,
            maxWidth: Int,
            maxHeight: Int
        ): Pair<Int, Int> {
            val aspectRatio = originalWidth.toFloat() / originalHeight.toFloat()

            return if (originalWidth > originalHeight) {
                // Immagine quadrata od orizzontale
                val newWidth = minOf(originalWidth, maxWidth)
                val newHeight = (newWidth / aspectRatio).toInt()
                Pair(newWidth, minOf(newHeight, maxHeight))
            } else {
                // Immagine verticale
                val newHeight = minOf(originalHeight, maxHeight)
                val newWidth = (newHeight * aspectRatio).toInt()
                Pair(minOf(newWidth, maxWidth), newHeight)
            }
        }

        // Funzione ausiliaria per calcolare le dimensioni di campionamento
        private fun calcolaDimensioniCampionamento(
            options: BitmapFactory.Options,
            reqWidth: Int,
            reqHeight: Int
        ): Int {
            // Altezza e larghezza della sorgente
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                val halfHeight = height / 2
                val halfWidth = width / 2

                // Calcola il più grande valore di inSampleSize che è una potenza di 2 e che
                // mantiene entrambe le dimensioni dell'altezza e della larghezza maggiori di
                // altezza e larghezza richieste
                while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2
                }
            }

            return inSampleSize
        }

        // Funzione ausiliaria per ruotare il bitmap sulla base dei metadati EXIF
        private fun ruotaImmagineSeNecessario(bitmap: Bitmap, exif: ExifInterface): Bitmap {
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            return when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> ruotaBitmap(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> ruotaBitmap(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> ruotaBitmap(bitmap, 270f)
                else -> bitmap
            }
        }

        // Funzione ausiliaria per ruotare il bitmap
        private fun ruotaBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
            val matrix = Matrix().apply {
                postRotate(degrees)
            }
            return Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        }
    }
}