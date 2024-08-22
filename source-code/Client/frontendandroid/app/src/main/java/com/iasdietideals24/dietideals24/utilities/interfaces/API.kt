package com.iasdietideals24.dietideals24.utilities.interfaces

import com.iasdietideals24.dietideals24.utilities.classes.data.AccountInfo
import com.iasdietideals24.dietideals24.utilities.classes.data.AccountInfoProfilo
import com.iasdietideals24.dietideals24.utilities.classes.data.DatiAnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.classes.data.DatiNotifica
import com.iasdietideals24.dietideals24.utilities.classes.data.DettagliAsta
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.lang.Long

interface API {
    /**
     * Il metodo recupera l'identificativo dell'account che ha effettuato l'accesso.
     * La password salvata è in formato hash, quindi dovrebbe essere convertita e confrontata con quella inserita dall'utente.
     * @param accountEmail Email dell'account che sta tentando di accedere.
     * @param accountPassword Password dell'account che sta tentando di accedere.
     * @param tipoAccount Indica il tipo di account che sta tentando di accedere.
     * @return L'identificativo dell'account che ha effettuato l'accesso. Se l'account non esiste, viene restituito 0.
     */
    @GET("accesso/accedi")
    fun accedi(
        @Query("email") accountEmail: String,
        @Query("password") accountPassword: String,
        @Query("tipoAccount") tipoAccount: String
    ): Call<Long>

    /**
     * Il metodo controlla se l'email è già associata a un account dello stesso tipo.
     * @param accountEmail Email dell'account che sta tentando di registrarsi.
     * @param tipoAccount Indica il tipo di account che sta tentando di registrarsi.
     * @return Un booleano che indica se l'email è già associata a un account dello stesso tipo.
     */
    @GET("registrazione/esisteEmail")
    fun esisteEmail(
        @Query("email") accountEmail: String,
        @Query("tipoAccount") tipoAccount: String
    ): Call<Boolean>

    /**
     * Il metodo controlla se l'email è già associata a un account di tipo diverso.
     * @param accountEmail Email dell'account che sta tentando di registrarsi.
     * @param accountPassword Password dell'account che sta tentando di registrarsi.
     * @param tipoAccount Indica il tipo di account che sta tentando di registrarsi.
     * @return Un booleano che indica se l'email è già associata a un account di tipo diverso.
     */
    @GET("registrazione/associaCreaProfilo")
    fun associaCreaProfilo(
        @Query("email") accountEmail: String,
        @Query("password") accountPassword: String,
        @Query("tipoAccount") tipoAccount: String
    ): Call<Boolean>

    /**
     * Il metodo recupera l'ID dell'account associato all'ID dell'account Facebook, ma solo se
     * l'utente ha un account dello stesso tipo che ha selezionato.
     * @param facebookId Identificativo dell'account Facebook dell'utente.
     * @param tipoAccount Indica il tipo di account che sta tentando di accedere o registrarsi.
     * @return L'identificativo dell'account
     */
    @GET("accountFacebook")
    fun accountFacebook(
        @Query("facebookId") facebookId: String,
        @Query("tipoAccount") tipoAccount: String
    ): Call<Long>

    /**
     * Registra i dati del nuovo account e li associa al profilo già esistente.
     * @param accountInfo Wrapper con le informazioni necessarie a creare il nuovo account.
     * @return L'identificativo dell'account appena creato ed associato.
     */
    @POST("registrazione/associazioneProfilo")
    fun associazioneProfilo(
        @Body accountInfo: AccountInfo
    ): Call<Long>

    /**
     * Registra i dati del nuovo account e crea un nuovo profilo da associare a tale account.
     * @param modelRegistrazione Wrapper con le informazioni necessarie a creare il nuovo account e il nuovo profilo.
     * @return L'identificativo dell'account appena creato.
     */
    @POST("registrazione/creazioneProfilo")
    fun creazioneProfilo(
        @Body dataClass: AccountInfoProfilo
    ): Call<Long>

    /**
     * Recupera l'elenco di tutte le aste con le quali può interagire l'utente che ha attualmente
     * effettuato il login.
     * @param idAccount Identificativo dell'account che ha effettuato l'accesso.
     * @return L'elenco delle aste da mostrare nella home.
     */
    @GET("home/recuperaAste")
    fun recuperaAste(
        @Query("idAccount") idAccount: Long
    ): Call<Array<DatiAnteprimaAsta>>

    /**
     * Recupera l'elenco di tutte le aste con le quali può interagire l'utente che ha attualmente
     * effettuato il login in base ai filtri di ricerca specificati.
     * @param idAccount Identificativo dell'account che ha effettuato l'accesso.
     * @param ricerca Stringa di ricerca specificata dall'utente.
     * @param filtro Filtro specificato dall'utente.
     * @return L'elenco delle aste da mostrare nella home dopo la ricerca e/o il filtro.
     */
    @GET("home/ricercaAste")
    fun ricercaAste(
        @Query("idAccount") idAccount: Long,
        @Query("ricerca") ricerca: String,
        @Query("filtro") filtro: String
    ): Call<Array<DatiAnteprimaAsta>>

    /**
     * Recupera i dettagli dell'asta specificata.
     * @param idAsta Identificativo dell'asta da recuperare.
     * @return I dettagli dell'asta specificata.
     */
    @GET("home/caricaAsta")
    fun caricaAsta(
        @Query("idAsta") idAsta: Long
    ): Call<DettagliAsta>

    /**
     * Recupera tutte le notifiche che sono state indirizzate all'utente che ha effettuato l'accesso.
     * @param idAccount Identificativo dell'account che ha effettuato l'accesso.
     * @return L'elenco delle notifiche da mostrare all'utente.
     */
    @GET("home/recuperaNotifiche")
    fun recuperaNotifiche(
        @Query("idAccount") idAccount: Long
    ): Call<Array<DatiNotifica>>
}