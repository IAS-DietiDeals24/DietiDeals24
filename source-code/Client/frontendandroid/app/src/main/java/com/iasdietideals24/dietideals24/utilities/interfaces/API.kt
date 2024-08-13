package com.iasdietideals24.dietideals24.utilities.interfaces

import com.iasdietideals24.dietideals24.utilities.classes.APIMode
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface API {
    /**
     * Il metodo recupera l'identificativo dell'account che ha effettuato l'accesso.
     * La password salvata è in formato hash, quindi dovrebbe essere convertita e confrontata con quella inserita dall'utente.
     * @param mode Indica se si sta effettuando la chiamata in ambiente di sviluppo o in produzione.
     * @param accountEmail Email dell'account che sta tentando di accedere.
     * @param accountPassword Password dell'account che sta tentando di accedere.
     * @param tipoAccount Indica il tipo di account che sta tentando di accedere.
     * @return Un intero che rappresenta l'identificativo dell'account che ha effettuato l'accesso. Se l'account non esiste, viene restituito 0.
     */
    @GET("accesso/accedi")
    fun accedi(
        @Header("MODE") mode: Int,
        @Query("email") accountEmail: String,
        @Query("password") accountPassword: String,
        @Query("tipoAccount") tipoAccount: String
    ): Call<Int>

    /**
     * Il metodo controlla se l'email è già associata a un account dello stesso tipo.
     * @param mode Indica se si sta effettuando la chiamata in ambiente di sviluppo o in produzione.
     * @param accountEmail Email dell'account che sta tentando di registrarsi.
     * @param tipoAccount Indica il tipo di account che sta tentando di registrarsi.
     * @return Un booleano che indica se l'email è già associata a un account dello stesso tipo.
     */
    @GET("registrazione/esisteEmail")
    fun esisteEmail(
        @Header("MODE") mode: Int,
        @Query("email") accountEmail: String,
        @Query("tipoAccount") tipoAccount: String
    ): Call<Boolean>

    /**
     * Il metodo controlla se l'email è già associata a un account di tipo diverso.
     * @param mode Indica se si sta effettuando la chiamata in ambiente di sviluppo o in produzione.
     * @param accountEmail Email dell'account che sta tentando di registrarsi.
     * @param accountPassword Password dell'account che sta tentando di registrarsi.
     * @param tipoAccount Indica il tipo di account che sta tentando di registrarsi.
     * @return Un booleano che indica se l'email è già associata a un account di tipo diverso.
     */
    @GET("registrazione/associaCreaProfilo")
    fun associaCreaProfilo(
        @Header("MODE") mode: Int,
        @Query("email") accountEmail: String,
        @Query("password") accountPassword: String,
        @Query("tipoAccount") tipoAccount: String
    ): Call<Boolean>
}