package com.iasdietideals24.dietideals24.utilities.interfaces

import com.iasdietideals24.dietideals24.utilities.classes.AccountInfo
import com.iasdietideals24.dietideals24.utilities.classes.AccountProfileInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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

    /**
     * Il metodo recupera l'ID dell'account associato all'ID dell'account Facebook, ma solo se
     * l'utente ha un account dello stesso tipo che ha selezionato.
     * @param mode Indica se si sta effettuando la chiamata in ambiente di sviluppo o in produzione.
     * @param facebookId Identificativo dell'account Facebook dell'utente.
     * @param tipoAccount Indica il tipo di account che sta tentando di accedere o registrarsi.
     * @return L'identificativo dell'account
     */
    @GET("accountFacebook")
    fun accountFacebook(
        @Header("MODE") mode: Int,
        @Query("facebookId") facebookId: String,
        @Query("tipoAccount") tipoAccount: String
    ): Call<Int>

    /**
     * Registra i dati del nuovo account e li associa al profilo già esistente.
     * @param mode Indica se si sta effettuando la chiamata in ambiente di sviluppo o in produzione.
     * @param accountInfo Wrapper con le informazioni necessarie a creare il nuovo account.
     * @return Indica se l'operazione di associazione è andata a buon fine.
     */
    @POST("registrazione/associazioneProfilo")
    fun associazioneProfilo(
        @Header("MODE") mode: Int,
        @Body accountInfo: AccountInfo
    ): Call<Boolean>

    /**
     * Registra i dati del nuovo account e crea un nuovo profilo da associare a tale account.
     * @param mode Indica se si sta effettuando la chiamata in ambiente di sviluppo o in produzione.
     * @param modelRegistrazione Wrapper con le informazioni necessarie a creare il nuovo account e il nuovo profilo.
     * @return Indica se l'operazione di creazione è andata a buon fine.
     */
    @POST("registrazione/creazioneProfilo")
    fun creazioneProfilo(
        @Header("MODE") mode: Int,
        @Body dataClass: AccountProfileInfo
    ): Call<Boolean>
}