package com.iasdietideals24.dietideals24.utilities.interfaces

import com.iasdietideals24.dietideals24.utilities.dto.AstaInversaDto
import com.iasdietideals24.dietideals24.utilities.dto.AstaSilenziosaDto
import com.iasdietideals24.dietideals24.utilities.dto.AstaTempoFissoDto
import com.iasdietideals24.dietideals24.utilities.dto.CompratoreDto
import com.iasdietideals24.dietideals24.utilities.dto.NotificaDto
import com.iasdietideals24.dietideals24.utilities.dto.OffertaInversaDto
import com.iasdietideals24.dietideals24.utilities.dto.OffertaSilenziosaDto
import com.iasdietideals24.dietideals24.utilities.dto.OffertaTempoFissoDto
import com.iasdietideals24.dietideals24.utilities.dto.ProfiloDto
import com.iasdietideals24.dietideals24.utilities.dto.VenditoreDto
import com.iasdietideals24.dietideals24.utilities.dto.exceptional.PutProfiloDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface API {
    /**
     * Il metodo recupera l'account associato all'ID dell'account Facebook, ma solo se l'utente ha
     * un account associato a questo profilo Facebook di tipo compratore.
     * @param facebookId Identificativo dell'account Facebook dell'utente.
     * @return [CompratoreDto] associato a questo account Facebook. Se non esiste, viene restituito un
     * account vuoto.
     */
    @GET("accounts/compratori")
    fun accountFacebookCompratore(
        @Query("facebookId") facebookId: String
    ): Call<CompratoreDto>

    /**
     * Il metodo recupera l'account associato all'ID dell'account Facebook, ma solo se l'utente ha
     * un account associato a questo profilo Facebook di tipo venditore.
     * @param facebookId Identificativo dell'account Facebook dell'utente.
     * @return [VenditoreDto] associato a questo account Facebook. Se non esiste, viene restituito un
     * account vuoto.
     */
    @GET("accounts/venditori")
    fun accountFacebookVenditore(
        @Query("facebookId") facebookId: String
    ): Call<VenditoreDto>

    /**
     * Il metodo recupera l'account compratore che ha effettuato l'accesso.
     * @param accountEmail Email dell'account che sta tentando di accedere.
     * @param accountPassword Password dell'account che sta tentando di accedere.
     * @return [CompratoreDto] che ha effettuato l'accesso. Se non esiste, viene restituito un account
     * vuoto.
     */
    @GET("accounts/compratori/{email}")
    fun accediCompratore(
        @Path("email") accountEmail: String,
        @Query("password") accountPassword: String
    ): Call<CompratoreDto>

    /**
     * Il metodo recupera l'account venditore che ha effettuato l'accesso.
     * @param accountEmail Email dell'account che sta tentando di accedere.
     * @param accountPassword Password dell'account che sta tentando di accedere.
     * @return [VenditoreDto] che ha effettuato l'accesso. Se non esiste, viene restituito un account
     * vuoto.
     */
    @GET("accounts/venditori/{email}")
    fun accediVenditore(
        @Path("email") accountEmail: String,
        @Query("password") accountPassword: String
    ): Call<VenditoreDto>

    /**
     * Crea un nuovo account compratore con le credenziali indicate.
     * @param accountEmail L'email dell'account da creare.
     * @param account Wrapper con le informazioni necessarie a creare il nuovo account.
     * @return [PutProfiloDto] appena creato. Se non è stato creato, viene restituito un account vuoto.
     */
    @POST("accounts/compratori/{email}")
    fun creazioneAccountCompratore(
        @Path("email") accountEmail: String,
        @Body account: PutProfiloDto
    ): Call<PutProfiloDto>

    /**
     * Crea un nuovo account venditore con le credenziali indicate.
     * @param accountEmail L'email dell'account da creare.
     * @param account Wrapper con le informazioni necessarie a creare il nuovo account.
     * @return [PutProfiloDto] appena creato. Se non è stato creato, viene restituito un account vuoto.
     */
    @POST("accounts/venditori/{email}")
    fun creazioneAccountVenditore(
        @Path("email") accountEmail: String,
        @Body account: PutProfiloDto
    ): Call<PutProfiloDto>

    /**
     * Recupera l'elenco di tutte le aste inverse create dal compratore che ha effettualo l'accesso.
     * @param accountEmail L'email dell'account che ha effettuato l'accesso.
     * @return [Set] di [AstaInversaDto] che contiene le aste create. Se non esistono, viene creato un
     * vettore vuoto.
     */
    @GET("aste/di-compratori/inverse")
    fun recuperaAsteCreateInverse(
        @Query("email") accountEmail: String
    ): Call<Set<AstaInversaDto>>

    /**
     * Recupera l'elenco di tutte le aste a tempo fisso create dal venditore che ha effettualo l'accesso.
     * @param accountEmail L'email dell'account che ha effettuato l'accesso.
     * @return [Set] di [AstaTempoFissoDto] che contiene le aste create. Se non esistono, viene creato un
     * vettore vuoto.
     */
    @GET("aste/di-venditori/tempo-fisso")
    fun recuperaAsteCreateTempoFisso(
        @Query("email") accountEmail: String
    ): Call<Set<AstaTempoFissoDto>>

    /**
     * Recupera l'elenco di tutte le aste silenziose create dal venditore che ha effettualo l'accesso.
     * @param accountEmail L'email dell'account che ha effettuato l'accesso.
     * @return [Set] di [AstaSilenziosaDto] che contiene le aste create. Se non esistono, viene creato un
     * vettore vuoto.
     */
    @GET("aste/di-venditori/silenziose")
    fun recuperaAsteCreateSilenziose(
        @Query("email") accountEmail: String
    ): Call<Set<AstaSilenziosaDto>>

    /**
     * Carica una nuova asta inversa.
     * @param asta Wrapper con le informazioni necessarie a creare l'asta.
     * @return [AstaInversaDto] appena creata. Se non è stata creata, viene restituita un'asta vuota.
     */
    @POST("aste/di-compratori/inverse")
    fun creaAstaInversa(
        @Body asta: AstaInversaDto
    ): Call<AstaInversaDto>

    /**
     * Carica una nuova asta silenziosa.
     * @param asta Wrapper con le informazioni necessarie a creare l'asta.
     * @return [AstaSilenziosaDto] appena creata. Se non è stata creata, viene restituita un'asta vuota.
     */
    @POST("aste/di-venditori/silenziose")
    fun creaAstaSilenziosa(
        @Body asta: AstaSilenziosaDto
    ): Call<AstaSilenziosaDto>

    /**
     * Carica una nuova asta a tempo fisso.
     * @param asta Wrapper con le informazioni necessarie a creare l'asta.
     * @return [AstaTempoFissoDto] appena creata. Se non è stata creata, viene restituita un'asta vuota.
     */
    @POST("aste/di-venditori/tempo-fisso")
    fun creaAstaTempoFisso(
        @Body asta: AstaTempoFissoDto
    ): Call<AstaTempoFissoDto>

    /**
     * Recupera i dettagli dell'asta a tempo fisso specificata.
     * @param idAsta Identificativo dell'asta da recuperare.
     * @return [AstaTempoFissoDto] richiesta. Se non esiste, viene restituita un'asta vuota.
     */
    @GET("aste/di-venditori/tempo-fisso/{idAsta}")
    fun caricaAstaTempoFisso(
        @Path("idAsta") idAsta: Long
    ): Call<AstaTempoFissoDto>

    /**
     * Recupera i dettagli dell'asta silenziosa specificata.
     * @param idAsta Identificativo dell'asta da recuperare.
     * @return [AstaSilenziosaDto] richiesta. Se non esiste, viene restituita un'asta vuota.
     */
    @GET("aste/di-venditori/silenziose/{idAsta}")
    fun caricaAstaSilenziosa(
        @Path("idAsta") idAsta: Long
    ): Call<AstaSilenziosaDto>

    /**
     * Recupera i dettagli dell'asta inversa specificata.
     * @param idAsta Identificativo dell'asta da recuperare.
     * @return [AstaInversaDto] richiesta. Se non esiste, viene restituita un'asta vuota.
     */
    @GET("aste/di-compratori/inverse/{idAsta}")
    fun caricaAstaInversa(
        @Path("idAsta") idAsta: Long
    ): Call<AstaInversaDto>

    /**
     * Carica tutti i dati di un profilo associato a un account.
     * @param accountEmail Email dell'account del quale recuperare il profilo.
     * @return [ProfiloDto] richiesto. Se non esiste, viene restituito un profilo vuoto.
     */
    @GET("profili")
    fun caricaProfiloDaAccount(
        @Query("email") accountEmail: String
    ): Call<ProfiloDto>

    /**
     * Recupera l'offerta più alta inviata a un'asta a tempo fisso.
     * @param idAsta Identificativo dell'asta della quale recuperare l'offerta.
     * @return [OffertaTempoFissoDto] con il valore più alto. Se non esiste, viene restituita un'offerta vuota.
     */
    @GET("offerte/di-venditori/tempo-fisso/findMax")
    fun recuperaOffertaPiuAlta(
        @Query("idAsta") idAsta: Long
    ): Call<OffertaTempoFissoDto>

    /**
     * Recupera l'offerta più alta inviata a un'asta a tempo fisso dall'utente.
     * @param idAsta Identificativo dell'asta della quale recuperare l'offerta.
     * @param accountEmail Email dell'account dell'utente.
     * @return [OffertaTempoFissoDto] con il valore più alto. Se non esiste, viene restituita un'offerta vuota.
     */
    @GET("offerte/di-venditori/tempo-fisso/findMax")
    fun recuperaOffertaPersonalePiuAltaTempoFisso(
        @Query("idAsta") idAsta: Long,
        @Query("email") accountEmail: String
    ): Call<OffertaTempoFissoDto>

    /**
     * Recupera l'offerta più alta inviata a un'asta a silenziosa dall'utente.
     * @param idAsta Identificativo dell'asta della quale recuperare l'offerta.
     * @param accountEmail Email dell'account dell'utente.
     * @return [OffertaSilenziosaDto] con il valore più alto. Se non esiste, viene restituita un'offerta vuota.
     */
    @GET("offerte/di-venditori/silenziose/findMax")
    fun recuperaOffertaPersonalePiuAltaSilenziosa(
        @Query("idAsta") idAsta: Long,
        @Query("email") accountEmail: String
    ): Call<OffertaSilenziosaDto>

    /**
     * Recupera l'offerta più bassa inviata a un'asta inversa.
     * @param idAsta Identificativo dell'asta della quale recuperare l'offerta.
     * @return [OffertaInversaDto] con il valore più basso. Se non esiste, viene restituita un'offerta vuota.
     */
    @GET("offerte/di-compratori/inverse/findMin")
    fun recuperaOffertaPiuBassa(
        @Query("idAsta") idAsta: Long
    ): Call<OffertaInversaDto>

    /**
     * Recupera l'offerta più bassa inviata a un'asta inversa da un utente.
     * @param idAsta Identificativo dell'asta della quale recuperare l'offerta.
     * @param accountEmail Email dell'account dell'utente.
     * @return [OffertaInversaDto] con il valore più basso. Se non esiste, viene restituita un'offerta vuota.
     */
    @GET("offerte/di-compratori/inverse/findMin")
    fun recuperaOffertaPersonalePiuBassaInversa(
        @Query("idAsta") idAsta: Long,
        @Query("email") accountEmail: String
    ): Call<OffertaInversaDto>

    /**
     * Invia un'offerta a un'asta inversa. Manda inoltre una notifica al proprietario dell'asta con
     * le informazioni dell'offerta.
     * @param offerta Wrapper con le informazioni necessarie a inviare l'offerta.
     * @param idAsta Identificativo dell'asta alla quale inviare l'offerta.
     * @return [OffertaInversaDto] appena inviata. Se non è stata inviata, viene restituita un'offerta vuota.
     */
    @POST("offerte/di-compratori/inverse")
    fun inviaOffertaInversa(
        @Body offerta: OffertaInversaDto,
        @Query("idAsta") idAsta: Long
    ): Call<OffertaInversaDto>

    /**
     * Invia un'offerta a un'asta a tempo fisso. Manda inoltre una notifica al proprietario
     * dell'asta con le informazioni dell'offerta.
     * @param offerta Wrapper con le informazioni necessarie a inviare l'offerta.
     * @param idAsta Identificativo dell'asta alla quale inviare l'offerta.
     * @return [OffertaTempoFissoDto] appena inviata. Se non è stata inviata, viene restituita un'offerta vuota.
     */
    @POST("offerte/di-venditori/tempo-fisso")
    fun inviaOffertaTempoFisso(
        @Body offerta: OffertaTempoFissoDto,
        @Query("idAsta") idAsta: Long
    ): Call<OffertaTempoFissoDto>

    /**
     * Invia un'offerta a un'asta silenziosa. Manda inoltre una notifica al proprietario dell'asta
     * con le informazioni dell'offerta.
     * @param offerta Wrapper con le informazioni necessarie a inviare l'offerta.
     * @param idAsta Identificativo dell'asta alla quale inviare l'offerta.
     * @return [OffertaSilenziosaDto] appena inviata. Se non è stata inviata, viene restituita un'offerta vuota.
     */
    @POST("offerte/di-venditori/silenziose")
    fun inviaOffertaSilenziosa(
        @Body offerta: OffertaSilenziosaDto,
        @Query("idAsta") idAsta: Long
    ): Call<OffertaSilenziosaDto>

    /**
     * Elimina un'asta inversa e tutti i suoi dati associati dalla base di dati. Manda inoltre
     * una notifica a coloro che hanno partecipato all'asta per avvisare della cancellazione.
     * @param idAsta Identificativo dell'asta da eliminare.
     */
    @DELETE("aste/di-compratori/inverse/{idAsta}")
    fun eliminaAstaInversa(
        @Path("idAsta") idAsta: Long
    ): Call<Unit>

    /**
     * Elimina un'asta a tempo fisso e tutti i suoi dati associati dalla base di dati. Manda inoltre
     * una notifica a coloro che hanno partecipato all'asta per avvisare della cancellazione.
     * @param idAsta Identificativo dell'asta da eliminare.
     */
    @DELETE("aste/di-venditori/tempo-fisso/{idAsta}")
    fun eliminaAstaTempoFisso(
        @Path("idAsta") idAsta: Long
    ): Call<Unit>

    /**
     * Elimina un'asta silenziosa e tutti i suoi dati associati dalla base di dati. Manda inoltre
     * una notifica a coloro che hanno partecipato all'asta per avvisare della cancellazione.
     * @param idAsta Identificativo dell'asta da eliminare.
     */
    @DELETE("aste/di-venditori/silenziose/{idAsta}")
    fun eliminaAstaSilenziosa(
        @Path("idAsta") idAsta: Long
    ): Call<Unit>

    /**
     * Il metodo recupera l'account venditore specificato.
     * @param accountEmail Email dell'account.
     * @return [VenditoreDto] richiesto. Se non esiste, viene restituito un account vuoto.
     */
    @GET("accounts/venditori/{email}")
    fun caricaAccountVenditore(
        @Path("email") accountEmail: String,
    ): Call<VenditoreDto>

    /**
     * Il metodo recupera l'account compratore specificato.
     * @param accountEmail Email dell'account.
     * @return [CompratoreDto] richiesto. Se non esiste, viene restituito un account vuoto.
     */
    @GET("accounts/compratori/{email}")
    fun caricaAccountCompratore(
        @Path("email") accountEmail: String,
    ): Call<CompratoreDto>

    /**
     * Il metodo controlla se l'email è già associata a un account compratore.
     * @param accountEmail Email dell'account che sta tentando di registrarsi.
     * @return Un [Boolean] che indica se l'email è già associata a un account dello stesso tipo.
     */
    @GET("accounts/compratori/{email}/exists")
    fun esisteEmailCompratore(
        @Path("email") accountEmail: String,
    ): Call<Boolean>

    /**
     * Il metodo controlla se l'email è già associata a un account venditore.
     * @param accountEmail Email dell'account che sta tentando di registrarsi.
     * @return Un [Boolean] che indica se l'email è già associata a un account dello stesso tipo.
     */
    @GET("accounts/venditori/{email}/exists")
    fun esisteEmailVenditore(
        @Path("email") accountEmail: String,
    ): Call<Boolean>

    /**
     * Il metodo controlla se il nome utente è già associata a un profilo.
     * @param nomeUtente Nome utente del profilo che sta tentando di registrarsi.
     * @return Un [Boolean] che indica se il nome utente è già associato a un profilo.
     */
    @GET("profili/{nomeUtente}/exists")
    fun esisteNomeUtente(
        @Path("nomeUtente") nomeUtente: String,
    ): Call<Boolean>

    /**
     * Il metodo controlla se l'email che si sta usando per creare l'account compratore è già associato
     * a un account di tipo venditore.
     * @param accountEmail Email dell'account che sta tentando di registrarsi.
     * @return Un [Boolean] che indica se l'email è già associata a un account di tipo diverso.
     */
    @GET("accounts/venditori/{email}")
    fun associaCreaProfiloCompratore(
        @Path("email") accountEmail: String,
    ): Call<VenditoreDto>

    /**
     * Il metodo controlla se l'email che si sta usando per creare l'account venditore è già associato
     * a un account di tipo compratore.
     * @param accountEmail Email dell'account che sta tentando di registrarsi.
     * @return Un [Boolean] che indica se l'email è già associata a un account di tipo diverso.
     */
    @GET("accounts/compratori/{email}")
    fun associaCreaProfiloVenditore(
        @Path("email") accountEmail: String,
    ): Call<CompratoreDto>

    /**
     * Recupera l'elenco di tutte le aste silenziose con le quali può interagire il compratore che ha
     * effettuato il login.
     * @return [Set] di [AstaSilenziosaDto] con le aste da mostrare nella home. Se non ne esistono,
     * viene restituito un insieme vuoto.
     */
    @GET("aste/di-venditori/silenziose")
    fun recuperaAsteSilenziose(): Call<Set<AstaSilenziosaDto>>

    /**
     * Recupera l'elenco di tutte le aste a tempo fisso con le quali può interagire il compratore che ha
     * effettuato il login.
     * @return [Set] di [AstaTempoFissoDto] con le aste da mostrare nella home. Se non ne esistono,
     * viene restituito un insieme vuoto.
     */
    @GET("aste/di-venditori/tempo-fisso")
    fun recuperaAsteTempoFisso(): Call<Set<AstaTempoFissoDto>>

    /**
     * Recupera l'elenco di tutte le aste inverse con le quali può interagire il venditore che ha
     * effettuato il login.
     * @return [Set] di [AstaInversaDto] con le aste da mostrare nella home. Se non ne esistono,
     * viene restituito un insieme vuoto.
     */
    @GET("aste/di-compratori/inverse")
    fun recuperaAsteInverse(): Call<Set<AstaInversaDto>>

    /**
     * Recupera l'elenco di tutte le aste silenziose con le quali può interagire l'utente che ha attualmente
     * effettuato il login in base ai filtri di ricerca specificati.
     * Il campo del filtro non sarà mai vuoto; il campo della ricerca potrebbe esserlo. In tal caso,
     * il campo della ricerca viene ignorato e viene restituito l'elenco delle aste che rispettano
     * solo il filtro.
     * Se entrambi i campi sono non vuoti, invece, verranno recuperate le aste che rispettano
     * entrambi i criteri.
     * @param ricerca Stringa di ricerca specificata dall'utente.
     * @param filtro Filtro specificato dall'utente.
     * @return [Set] di [AstaSilenziosaDto] con le aste da mostrare nella home dopo la ricerca e/o il
     * filtraggio. Se non ne esistono, viene restituito un insieme vuoto.
     */
    @GET("aste/di-venditori/silenziose")
    fun ricercaAsteSilenziose(
        @Query("ricerca") ricerca: String,
        @Query("filtro") filtro: String
    ): Call<Set<AstaSilenziosaDto>>

    /**
     * Recupera l'elenco di tutte le aste a tempo fisso con le quali può interagire l'utente che ha attualmente
     * effettuato il login in base ai filtri di ricerca specificati.
     * Il campo del filtro non sarà mai vuoto; il campo della ricerca potrebbe esserlo. In tal caso,
     * il campo della ricerca viene ignorato e viene restituito l'elenco delle aste che rispettano
     * solo il filtro.
     * Se entrambi i campi sono non vuoti, invece, verranno recuperate le aste che rispettano
     * entrambi i criteri.
     * @param ricerca Stringa di ricerca specificata dall'utente.
     * @param filtro Filtro specificato dall'utente.
     * @return [Set] di [AstaTempoFissoDto] con le aste da mostrare nella home dopo la ricerca e/o il
     * filtraggio. Se non ne esistono, viene restituito un insieme vuoto.
     */
    @GET("aste/di-venditori/tempo-fisso")
    fun ricercaAsteTempoFisso(
        @Query("ricerca") ricerca: String,
        @Query("filtro") filtro: String
    ): Call<Set<AstaTempoFissoDto>>

    /**
     * Recupera l'elenco di tutte le aste inverse con le quali può interagire l'utente che ha attualmente
     * effettuato il login in base ai filtri di ricerca specificati.
     * Il campo del filtro non sarà mai vuoto; il campo della ricerca potrebbe esserlo. In tal caso,
     * il campo della ricerca viene ignorato e viene restituito l'elenco delle aste che rispettano
     * solo il filtro.
     * Se entrambi i campi sono non vuoti, invece, verranno recuperate le aste che rispettano
     * entrambi i criteri.
     * @param ricerca Stringa di ricerca specificata dall'utente.
     * @param filtro Filtro specificato dall'utente.
     * @return [Set] di [AstaInversaDto] con le aste da mostrare nella home dopo la ricerca e/o il
     * filtraggio. Se non ne esistono, viene restituito un insieme vuoto.
     */
    @GET("aste/di-venditori/inverse")
    fun ricercaAsteInverse(
        @Query("ricerca") ricerca: String,
        @Query("filtro") filtro: String
    ): Call<Set<AstaInversaDto>>

    /**
     * Recupera tutte le notifiche che sono state indirizzate all'utente che ha effettuato l'accesso.
     * @param email Identificativo dell'account che ha effettuato l'accesso.
     * @return [Set] di [NotificaDto] con le notifiche da mostrare all'utente. Se non ne esistono,
     * viene restituito un insieme vuoto.
     */
    @GET("notifiche")
    fun recuperaNotifiche(
        @Query("email") email: String
    ): Call<Set<NotificaDto>>

    /**
     * Aggiorna i dati di un profilo.
     * @param profilo Wrapper con le informazioni necessarie ad aggiornare il profilo.
     * @param nomeUtente Identificativo del profilo da aggiornare.
     * @return Il [ProfiloDto] appena modificato. Se non è stato modificato, viene restituito il profilo originale.
     */
    @PATCH("profili/{nomeUtente}")
    fun aggiornaProfilo(
        @Body profilo: ProfiloDto,
        @Path("nomeUtente") nomeUtente: String
    ): Call<ProfiloDto>

    /**
     * Aggiorna i dati di un'asta silenziosa.
     * @param asta Wrapper con le informazioni necessarie ad aggiornare l'asta.
     * @param idAsta Identificativo dell'asta da aggiornare.
     * @return L'[AstaSilenziosaDto] appena modificata. Se non è stata modificata, viene restituita l'asta originale.
     */
    @PATCH("aste/di-venditori/silenziose/{idAsta}")
    fun aggiornaAstaSilenziosa(
        @Body asta: AstaSilenziosaDto,
        @Path("idAsta") idAsta: Long
    ): Call<AstaSilenziosaDto>

    /**
     * Aggiorna i dati di un'asta a tempo fisso.
     * @param asta Wrapper con le informazioni necessarie ad aggiornare l'asta.
     * @param idAsta Identificativo dell'asta da aggiornare.
     * @return L'[AstaTempoFissoDto] appena modificata. Se non è stata modificata, viene restituita l'asta originale.
     */
    @PATCH("aste/di-venditori/tempo-fisso/{idAsta}")
    fun aggiornaAstaTempoFisso(
        @Body asta: AstaTempoFissoDto,
        @Path("idAsta") idAsta: Long
    ): Call<AstaTempoFissoDto>

    /**
     * Aggiorna i dati di un'asta inversa.
     * @param asta Wrapper con le informazioni necessarie ad aggiornare l'asta.
     * @param idAsta Identificativo dell'asta da aggiornare.
     * @return L'[AstaInversaDto] appena modificata. Se non è stata modificata, viene restituita l'asta originale.
     */
    @PATCH("aste/di-venditori/inverse/{idAsta}")
    fun aggiornaAstaInversa(
        @Body asta: AstaInversaDto,
        @Path("idAsta") idAsta: Long
    ): Call<AstaInversaDto>

    /**
     * Recupera tutte le offerte inviate a un'asta inversa.
     * @param idAsta Identificativo dell'asta della quale recuperare le offerte.
     * @return Un [Set] di [OffertaInversaDto] con le offerte dell'asta. Se non ne esistono,
     * viene restituito un insieme vuoto.
     */
    @GET("offerte/di-compratori/inverse")
    fun recuperaOfferteInverse(
        @Query("idAsta") idAsta: Long
    ): Call<Set<OffertaInversaDto>>

    /**
     * Recupera tutte le offerte inviate a un'asta silenziosa.
     * @param idAsta Identificativo dell'asta della quale recuperare le offerte.
     * @return Un [Set] di [OffertaSilenziosaDto] con le offerte dell'asta. Se non ne esistono,
     * viene restituito un insieme vuoto.
     */
    @GET("offerte/di-venditori/silenziose")
    fun recuperaOfferteSilenziose(
        @Query("idAsta") idAsta: Long
    ): Call<Set<OffertaSilenziosaDto>>

    /**
     * Recupera tutte le offerte inviate a un'asta a tempo fisso.
     * @param idAsta Identificativo dell'asta della quale recuperare le offerte.
     * @return Un [Set] di [OffertaTempoFissoDto] con le offerte dell'asta. Se non ne esistono,
     * viene restituito un insieme vuoto.
     */
    @GET("offerte/di-venditori/tempo-fisso")
    fun recuperaOfferteTempoFisso(
        @Query("idAsta") idAsta: Long
    ): Call<Set<OffertaTempoFissoDto>>

    /**
     * Accetta un'offerta di un'asta silenziosa e, nella stessa transazione, rifiuta tutte le altre
     * offerte per la stessa asta. Manda inoltre una notifica a chi ha avanzato le offerte per
     * avvisare dell'esito.
     * @param idOfferta Identificativo dell'offerta da accettare.
     * @return Un [Boolean] che indica se l'operazione la transazione ha avuto successo.
     */
    @PATCH("offerte/di-venditori/silenziose/{idOfferta}")
    fun accettaOfferta(
        @Body offerta: OffertaSilenziosaDto,
        @Path("idOfferta") idOfferta: Long
    ): Call<Boolean>

    /**
     * Rifiuta un'offerta di un'asta silenziosa. Manda inoltre una notifica a chi ha avanzato l'offerta
     * per avvisare dell'esito.
     * @param idOfferta Identificativo dell'offerta da accettare.
     * @return Un [Boolean] che indica se l'operazione la transazione ha avuto successo.
     */
    @PATCH("offerte/di-venditori/silenziose/{idOfferta}")
    fun rifiutaOfferta(
        @Body offerta: OffertaSilenziosaDto,
        @Path("idOfferta") idOfferta: Long
    ): Call<Boolean>

    /**
     * Recupera l'elenco di tutte le aste silenziose alle quali il compratore ha inviato almeno un'offerta.
     * @param email Identificativo dell'account che ha effettuato l'accesso.
     * @return [Set] di [AstaSilenziosaDto] con le aste alle quali l'utente ha partecipato. Se non ne esistono,
     * viene restituito un insieme vuoto.
     */
    @GET("aste/di-venditori/silenziose")
    fun recuperaPartecipazioniSilenziose(
        @Query("email") email: String
    ): Call<Set<AstaSilenziosaDto>>

    /**
     * Recupera l'elenco di tutte le aste a tempo fisso alle quali il compratore ha inviato almeno un'offerta.
     * @param email Identificativo dell'account che ha effettuato l'accesso.
     * @return [Set] di [AstaTempoFissoDto] con le aste alle quali l'utente ha partecipato. Se non ne esistono,
     * viene restituito un insieme vuoto.
     */
    @GET("aste/di-venditori/tempo-fisso")
    fun recuperaPartecipazioniTempoFisso(
        @Query("email") email: String
    ): Call<Set<AstaTempoFissoDto>>

    /**
     * Recupera l'elenco di tutte le aste inverse alle quali il venditore ha inviato almeno un'offerta.
     * @param email Identificativo dell'account che ha effettuato l'accesso.
     * @return [Set] di [AstaInversaDto] con le aste alle quali l'utente ha partecipato. Se non ne esistono,
     * viene restituito un insieme vuoto.
     */
    @GET("aste/di-compratori/inverse")
    fun recuperaPartecipazioniInverse(
        @Query("email") email: String
    ): Call<Set<AstaInversaDto>>
}