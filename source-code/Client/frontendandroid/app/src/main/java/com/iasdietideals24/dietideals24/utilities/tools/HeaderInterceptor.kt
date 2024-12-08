package com.iasdietideals24.dietideals24.utilities.tools

import com.iasdietideals24.dietideals24.utilities.repositories.AuthRepository
import okhttp3.Interceptor
import okhttp3.Response
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

        // Esegui la richiesta e ottieni la risposta
        val risposta = chain.proceed(richiesta)

        // Se ottengo un non autorizzato, il token di accesso è scaduto
        if (risposta.code == 401) {
            synchronized(lock) {
                // Usa un blocco per evitare che la chiamata per aggiornare il token causi una
                // ricorsione infinita
                if (!isRefreshing) {
                    isRefreshing = true

                    try {
                        val nuovaRisposta = authRepository.aggiornaAccessToken(CurrentUser.rToken)

                        // Se ho recuperato correttamente il token, aggiorna quello salvato
                        if (nuovaRisposta.isSuccessful) {
                            val nuovoToken = nuovaRisposta.body()?.authToken ?: ""
                            CurrentUser.aToken = nuovoToken

                            // Chiudo la risposta precedente
                            risposta.close()

                            // Crea una nuova richiesta con il token appena recuperato
                            val nuovaRichiesta = richiestaOriginale.newBuilder()
                                .removeHeader("Authorization")
                                .addHeader("Authorization", "Bearer $nuovoToken")
                                .build()

                            // Riprova a inviare la richiesta
                            return chain.proceed(nuovaRichiesta)
                        }
                    } finally {
                        // Sblocca dopo aver terminato
                        isRefreshing = false
                    }
                }
            }
        }

        // Restituisci la risposta originale se tutto è ok oppure sono ancora non autorizzato
        // dopo aver fatto il refresh
        return risposta
    }
}