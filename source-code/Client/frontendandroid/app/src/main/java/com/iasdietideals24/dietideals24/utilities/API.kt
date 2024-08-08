package com.iasdietideals24.dietideals24.utilities

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface API {
    /**
     * Il metodo recupera l'identificativo dell'account che ha effettuato l'accesso.
     * La password salvata è in formato hash, quindi dovrebbe essere convertita e confrontata con quella inserita dall'utente.
     */
    @GET("accesso/accedi")
    fun accedi(
        @Query("email") accountEmail: String,
        @Query("password") accountPassword: String,
        @Query("tipoAccount") tipoAccount: String
    ): Call<Int>

    /**
     * Il metodo controlla se l'email è già associata a un account dello stesso tipo.
     */
    @GET("registrazione/esisteEmail")
    fun esisteEmail(
        @Query("email") accountEmail: String,
        @Query("tipoAccount") tipoAccount: String
    ): Call<Boolean>

    /**
     * Il metodo registra l'account con i dati forniti.
     * Se un account con stessa email ma tipo differente esiste già, lo aggiorna per segnalare la presenza di entrambi gli account.
     * Restituisce poi se è stato trovato un account di tipo differente con la stessa email e l'identificativo dell'account (appena creato o del vecchio)
     * La password dovrebbe essere convertita in formato hash prima di essere salvata.
     */
    @GET("registrazione/registra")
    fun registra(
        @Query("email") accountEmail: String,
        @Query("password") accountPassword: String,
        @Query("tipoAccount") tipoAccount: String
    ): Call<Pair<Boolean, Int>>
}
