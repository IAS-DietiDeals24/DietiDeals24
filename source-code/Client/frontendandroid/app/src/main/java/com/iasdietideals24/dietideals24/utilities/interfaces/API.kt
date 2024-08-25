package com.iasdietideals24.dietideals24.utilities.interfaces

import com.iasdietideals24.dietideals24.utilities.classes.data.Account
import com.iasdietideals24.dietideals24.utilities.classes.data.AccountProfilo
import com.iasdietideals24.dietideals24.utilities.classes.data.AnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.classes.data.Asta
import com.iasdietideals24.dietideals24.utilities.classes.data.DettagliAsta
import com.iasdietideals24.dietideals24.utilities.classes.data.Notifica
import com.iasdietideals24.dietideals24.utilities.classes.data.Offerta
import com.iasdietideals24.dietideals24.utilities.classes.data.Profilo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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
     * @param account Wrapper con le informazioni necessarie a creare il nuovo account.
     * @return L'identificativo dell'account appena creato ed associato.
     */
    @POST("registrazione/associazioneProfilo")
    fun associazioneProfilo(
        @Body account: Account
    ): Call<Long>

    /**
     * Registra i dati del nuovo account e crea un nuovo profilo da associare a tale account.
     * @param accountProfilo Wrapper con le informazioni necessarie a creare il nuovo account e il nuovo profilo.
     * @return L'identificativo dell'account appena creato.
     */
    @POST("registrazione/creazioneProfilo")
    fun creazioneProfilo(
        @Body accountProfilo: AccountProfilo
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
    ): Call<Array<AnteprimaAsta>>

    /**
     * Recupera l'elenco di tutte le aste con le quali può interagire l'utente che ha attualmente
     * effettuato il login in base ai filtri di ricerca specificati.
     * Il campo del filtro non sarà mai vuoto; il campo della ricerca potrebbe esserlo. In tal caso,
     * il campo della ricerca viene ignorato e viene restituito l'elenco delle aste che rispettano
     * solo il filtro.
     * Se entrambi i campi sono non nulli, invece, verranno recuperate le aste che rispettano
     * entrambi i criteri.
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
    ): Call<Array<AnteprimaAsta>>

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
    ): Call<Array<Notifica>>

    /**
     * Carica una nuova asta all'interno del database.
     * @param dataClass Wrapper con le informazioni necessarie a creare l'asta.
     * @return L'identificativo dell'asta appena creata.
     */
    @POST("home/creaAsta")
    fun creaAsta(
        @Body asta: Asta
    ): Call<Boolean>

    /**
     * Carica tutti i dati di un profilo collegato ad un account.
     * @param idAccount Identificativo dell'account del quale recuperare il profilo.
     * @return Le informazioni da mostrare sul profilo, con altre informazioni importanti.
     */
    @GET("home/caricaProfilo")
    fun caricaProfilo(
        @Query("idAccount") idAccount: Long
    ): Call<Profilo>

    /**
     * Aggiorna i dati di un profilo.
     * @param profilo Wrapper con le informazioni necessarie a aggiornare il profilo.
     * @return Se l'operazione di aggiornamento ha avuto successo.
     */
    @PUT("home/aggiornaProfilo")
    fun aggiornaProfilo(
        @Body profilo: Profilo
    ): Call<Boolean>

    /**
     * Invia un'offerta ad un'asta.
     * @param offerta Wrapper con le informazioni necessarie ad inviare l'offerta.
     * @return Se l'operazione di invio ha avuto successo.
     */
    @POST("home/inviaOfferta")
    fun inviaOfferta(
        @Body offerta: Offerta
    ): Call<Boolean>

    /**
     * Elimina un'asta e tutti i suoi dati associati dalla base di dati.
     * @param idAsta Identificativo dell'asta da eliminare.
     * @return Se l'operazione di eliminazione ha avuto successo.
     */
    @DELETE("home/eliminaAsta")
    fun eliminaAsta(
        @Query("idAsta") idAsta: Long
    ): Call<Boolean>

    /**
     * Aggiorna i dati di un'asta.
     * @param asta Wrapper con le informazioni necessarie a aggiornare l'asta.
     * @return Se l'operazione di aggiotnamento ha avuto successo.
     */
    @PUT("home/aggiornaAsta")
    fun aggiornaAsta(
        @Body asta: Asta
    ): Call<Boolean>
}