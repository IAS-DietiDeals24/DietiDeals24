package com.iasdietideals24.dietideals24.utilities.tools

import android.util.Log
import com.iasdietideals24.dietideals24.utilities.repositories.AuthRepository
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import org.koin.java.KoinJavaComponent.inject

object HeaderInterceptor : Interceptor {
    private val authRepository: AuthRepository by inject(AuthRepository::class.java)
    private var isRefreshing = false
    private val lock = Any()

    override fun intercept(chain: Interceptor.Chain): Response {
        val richiestaOriginale = chain.request()

        // Richiesta iniziale, con header se ho il token di accesso o senza se non ce l'ho
        val richiesta = if (CurrentUser.aToken.isNotEmpty()) {
            richiestaOriginale.newBuilder()
                .addHeader("Authorization", "Bearer ${CurrentUser.aToken}")
                .build()
        } else {
            richiestaOriginale
        }

        // Scrivi la richiesta nei log
        var buffer = Buffer()
        richiesta.body?.writeTo(buffer)
        Log.d(
            "HeaderInterceptor",
            "Method: ${richiesta.method}, Url: ${richiesta.url}, Body: ${buffer.readUtf8()}"
        )

        // Esegui la richiesta e ottieni la risposta
        val risposta = try {
            chain.proceed(richiesta)
        } catch (e: Exception) {
            return Response.Builder()
                .request(richiesta)
                .protocol(Protocol.HTTP_2)
                .code(408)
                .message("Connection lost")
                .body("".toResponseBody())
                .build()
        }

        // Se ottengo un non autorizzato, il token di accesso è scaduto
        if (risposta.code == 401) {
            synchronized(lock) {
                // Usa un blocco per evitare che la chiamata per aggiornare il token causi una
                // ricorsione infinita
                if (!isRefreshing) {
                    isRefreshing = true

                    try {
                        val richiestaToken = authRepository.aggiornaAccessToken(CurrentUser.rToken)

                        // Se ho recuperato correttamente il token, aggiorna quello salvato
                        if (richiestaToken.isSuccessful) {
                            val nuovoToken = richiestaToken.body()?.authToken ?: ""
                            CurrentUser.aToken = nuovoToken

                            // Chiudo la risposta precedente
                            risposta.close()

                            // Crea una nuova richiesta con il token appena recuperato
                            val nuovaRichiesta = richiestaOriginale.newBuilder()
                                .removeHeader("Authorization")
                                .addHeader("Authorization", "Bearer $nuovoToken")
                                .build()

                            // Scrivi la richiesta nei log
                            buffer = Buffer()
                            richiesta.body?.writeTo(buffer)
                            Log.d(
                                "HeaderInterceptor",
                                "Method: ${richiesta.method}, Url: ${richiesta.url}, Body: ${buffer.readUtf8()}"
                            )

                            // Riprova a inviare la richiesta
                            val nuovaRisposta = try {
                                chain.proceed(nuovaRichiesta)
                            } catch (e: Exception) {
                                return Response.Builder()
                                    .request(nuovaRichiesta)
                                    .protocol(Protocol.HTTP_2)
                                    .code(408)
                                    .message("Connection lost")
                                    .body("".toResponseBody())
                                    .build()
                            }

                            // Scrivi la risposta nei log
                            Log.d(
                                "HeaderInterceptor",
                                nuovaRisposta.peekBody(Long.MAX_VALUE).string()
                            )

                            return nuovaRisposta
                        }
                    } finally {
                        // Sblocca dopo aver terminato
                        isRefreshing = false
                    }
                }
            }
        }

        // Scrivi la risposta nei log
        Log.d("HeaderInterceptor", risposta.peekBody(Long.MAX_VALUE).string())

        // Restituisci la risposta originale se tutto è ok oppure sono ancora non autorizzato
        // dopo aver fatto il refresh
        return risposta
    }
}