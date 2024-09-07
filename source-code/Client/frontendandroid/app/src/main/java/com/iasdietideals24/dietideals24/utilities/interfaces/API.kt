package com.iasdietideals24.dietideals24.utilities.interfaces

import com.iasdietideals24.dietideals24.utilities.classes.data.Account
import com.iasdietideals24.dietideals24.utilities.classes.data.AccountProfilo
import com.iasdietideals24.dietideals24.utilities.classes.data.AnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.classes.data.Asta
import com.iasdietideals24.dietideals24.utilities.classes.data.DettagliAsta
import com.iasdietideals24.dietideals24.utilities.classes.data.Notifica
import com.iasdietideals24.dietideals24.utilities.classes.data.Offerta
import com.iasdietideals24.dietideals24.utilities.classes.data.OffertaRicevuta
import com.iasdietideals24.dietideals24.utilities.classes.data.Profilo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

@Suppress("UNUSED")
interface API {
    /**
     * Il metodo recupera l'identificativo dell'account che ha effettuato l'accesso.
     * La password salvata è in formato hash, quindi dovrebbe essere convertita e confrontata con quella inserita dall'utente.
     * @param accountEmail Email dell'account che sta tentando di accedere.
     * @param accountPassword Password dell'account che sta tentando di accedere.
     * @param tipoAccount Indica il tipo di account che sta tentando di accedere.
     * @return L'identificativo dell'account che ha effettuato l'accesso. Se l'account non esiste, viene restituita la stringa vuota.
     */
    @GET("accesso/accedi")
    fun accedi(
        @Query("email") accountEmail: String,
        @Query("password") accountPassword: String,
        @Query("tipoAccount") tipoAccount: String
    ): Call<String>

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
     * Il metodo controlla se il nome utente è già associata a un profilo.
     * @param nomeUtente Nome utente del profilo che sta tentando di registrarsi.
     * @return Un booleano che indica se il nome utente è già associato a un profilo.
     */
    @GET("registrazione/esisteNomeUtente")
    fun esisteNomeUtente(
        @Query("nomeUtente") nomeUtente: String,
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
     * @return L'identificativo dell'account associato a questo account Facebook. Se l'account non esiste, viene restituita la stringa vuota.
     */
    @GET("accountFacebook")
    fun accountFacebook(
        @Query("facebookId") facebookId: String,
        @Query("tipoAccount") tipoAccount: String
    ): Call<String>

    /**
     * Registra i dati del nuovo account e li associa al profilo già esistente.
     * @param account Wrapper con le informazioni necessarie a creare il nuovo account.
     * @return L'identificativo dell'account appena creato ed associato.
     */
    @POST("registrazione/associazioneProfilo")
    fun associazioneProfilo(
        @Body account: Account
    ): Call<String>

    /**
     * Registra i dati del nuovo account e crea un nuovo profilo da associare a tale account.
     * @param accountProfilo Wrapper con le informazioni necessarie a creare il nuovo account e il nuovo profilo.
     * @return L'identificativo dell'account appena creato.
     */
    @POST("registrazione/creazioneProfilo")
    fun creazioneProfilo(
        @Body accountProfilo: AccountProfilo
    ): Call<String>

    /**
     * Recupera l'elenco di tutte le aste con le quali può interagire l'utente che ha attualmente
     * effettuato il login.
     * Attenzione: il campo "offerta" deve conservare l'offerta più alta/bassa che è stata
     * presentata per l'asta.
     * @param nomeUtente Identificativo dell'account che ha effettuato l'accesso.
     * @return L'elenco delle aste da mostrare nella home.
     */
    @GET("home/recuperaAste")
    fun recuperaAste(
        @Query("nomeUtente") nomeUtente: String
    ): Call<Array<AnteprimaAsta>>

    /**
     * Recupera l'elenco di tutte le aste con le quali può interagire l'utente che ha attualmente
     * effettuato il login in base ai filtri di ricerca specificati.
     * Il campo del filtro non sarà mai vuoto; il campo della ricerca potrebbe esserlo. In tal caso,
     * il campo della ricerca viene ignorato e viene restituito l'elenco delle aste che rispettano
     * solo il filtro.
     * Se entrambi i campi sono non nulli, invece, verranno recuperate le aste che rispettano
     * entrambi i criteri.
     * Attenzione: il campo "offerta" deve conservare l'offerta più alta/bassa che è stata
     * presentata per l'asta.
     * @param nomeUtente Identificativo dell'account che ha effettuato l'accesso.
     * @param ricerca Stringa di ricerca specificata dall'utente.
     * @param filtro Filtro specificato dall'utente.
     * @return L'elenco delle aste da mostrare nella home dopo la ricerca e/o il filtro.
     */
    @GET("home/ricercaAste")
    fun ricercaAste(
        @Query("nomeUtente") nomeUtente: String,
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
     * @param nomeUtente Identificativo dell'account che ha effettuato l'accesso.
     * @return L'elenco delle notifiche da mostrare all'utente.
     */
    @GET("home/recuperaNotifiche")
    fun recuperaNotifiche(
        @Query("nomeUtente") nomeUtente: String
    ): Call<Array<Notifica>>

    /**
     * Carica una nuova asta all'interno del database.
     * @param asta Wrapper con le informazioni necessarie a creare l'asta.
     * @return L'identificativo dell'asta appena creata.
     */
    @POST("home/creaAsta")
    fun creaAsta(
        @Body asta: Asta
    ): Call<Boolean>

    /**
     * Carica tutti i dati di un profilo collegato ad un account.
     * @param nomeUtente Identificativo dell'account del quale recuperare il profilo.
     * @return Le informazioni da mostrare sul profilo, con altre informazioni importanti.
     */
    @GET("home/caricaProfilo")
    fun caricaProfilo(
        @Query("nomeUtente") nomeUtente: String
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
     * Invia un'offerta ad un'asta. Manda inoltre una notifica al proprietario dell'asta con le
     * informazioni dell'offerta.
     * @param offerta Wrapper con le informazioni necessarie ad inviare l'offerta.
     * @return Se l'operazione di invio ha avuto successo.
     */
    @POST("home/inviaOfferta")
    fun inviaOfferta(
        @Body offerta: Offerta
    ): Call<Boolean>

    /**
     * Elimina un'asta e tutti i suoi dati associati dalla base di dati. Manda inoltre una notifica
     * a coloro che hanno partecipato all'asta per avvisare della cancellazione.
     * @param idAsta Identificativo dell'asta da eliminare.
     * @return Se l'operazione di eliminazione ha avuto successo.
     */
    @DELETE("home/eliminaAsta")
    fun eliminaAsta(
        @Query("idAsta") idAsta: Long
    ): Call<Boolean>

    /**
     * Aggiorna i dati di un'asta. Manda inoltre una notifica a coloro che hanno partecipato all'asta
     * per avvisare dell'aggiornamento.
     * @param asta Wrapper con le informazioni necessarie a aggiornare l'asta.
     * @return Se l'operazione di aggiornamento ha avuto successo.
     */
    @PUT("home/aggiornaAsta")
    fun aggiornaAsta(
        @Body asta: Asta
    ): Call<Boolean>

    /**
     * Recupera tutte le offerte inviate ad un'asta.
     * @param idAsta Identificativo dell'asta della recuperare le offerte.
     * @return L'elenco delle offerte da mostrare all'utente.
     */
    fun recuperaOfferte(
        @Query("idAsta") idAsta: Long
    ): Call<Array<OffertaRicevuta>>

    /**
     * Accetta un'offerta di un'asta silenziosa e, nella stessa transazione, rifiuta tutte le altre
     * offerte per la stessa asta. Manda inoltre una notifica a chi ha avanzato le offerte per
     * avvisare dell'esito.
     * @param idOfferta Identificativo dell'offerta da accettare.
     * @return Se l'operazione la transazione ha avuto successo.
     */
    @PUT("home/accettaOfferta")
    fun accettaOfferta(
        @Query("idOfferta") idOfferta: Long
    ): Call<Boolean>

    /**
     * Rifiuta un'offerta di un'asta silenziosa. Manda inoltre una notifica a chi ha avanzato l'offerta
     * per avvisare dell'esito.
     * @param idOfferta Identificativo dell'offerta da accettare.
     * @return Se l'operazione di riifuto ha avuto successo.
     */
    @PUT("home/rifiutaOfferta")
    fun rifiutaOfferta(
        @Query("idOfferta") idOfferta: Long
    ): Call<Boolean>

    /**
     * Recupera l'elenco di tutte le aste alle quali l'utente che ha attualmente effettuato l'accesso
     * ha inviato almeno un'offerta.
     * Attenzione: il campo "offerta" deve conservare l'offerta più alta/bassa che l'utente che ha
     * attualmente effettuato l'accesso è stata presentata per l'asta.
     * @param nomeUtente Identificativo dell'account che ha effettuato l'accesso.
     * @return L'elenco delle aste alle quali l'utente ha partecipato.
     */
    @GET("home/recuperaPartecipazioni")
    fun recuperaPartecipazioni(
        @Query("nomeUtente") nomeUtente: String
    ): Call<Array<AnteprimaAsta>>

    /**
     * Recupera l'elenco di tutte le aste create dall'utente che ha attualmente effettuato l'accesso.
     * @param nomeUtente Identificativo dell'account che ha effettuato l'accesso.
     * @return L'elenco delle aste create dall'utente.
     */
    @GET("home/recuperaAsteCreate")
    fun recuperaAsteCreate(
        @Query("nomeUtente") nomeUtente: String
    ): Call<Array<AnteprimaAsta>>
}