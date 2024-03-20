package com.iasdietideals24.dietideals24.registrazione

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.eccezioni.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.eccezioni.EccezioneCollegamentoSocialNonRiuscito
import com.iasdietideals24.dietideals24.eccezioni.EccezioneEmailNonValida
import com.iasdietideals24.dietideals24.eccezioni.EccezioneEmailUsata
import com.iasdietideals24.dietideals24.eccezioni.EccezionePasswordNonSicura
import com.iasdietideals24.dietideals24.model.Profilo
import com.iasdietideals24.dietideals24.model.account.Account
import com.iasdietideals24.dietideals24.model.account.AccountCompratore
import com.iasdietideals24.dietideals24.model.account.AccountVenditore
import com.iasdietideals24.dietideals24.scelteIniziali.ControllerScelteIniziali
import java.sql.Date


class ControllerRegistrazione : AppCompatActivity() {
    private lateinit var _tipoAccountDaRegistrare: String
    private lateinit var _accountDaRegistrare: Account

    var tipoAccountDaRegistrare: String
        get() = _tipoAccountDaRegistrare
        set(valore) {
            _tipoAccountDaRegistrare = valore
        }

    private var accountDaRegistrare: Account
        get() = _accountDaRegistrare
        set(valore) {
            _accountDaRegistrare = valore
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recuperaAttributiPassati()

        mostraRegistrazione()
    }

    private fun recuperaAttributiPassati() {
        val vecchiaAttivita = intent
        val dati = vecchiaAttivita.extras
        val datoTipoAccount = dati?.getString("tipoAccount")
        val stringaTipoAccount = datoTipoAccount.toString()
        tipoAccountDaRegistrare = stringaTipoAccount
        dati?.remove("tipoAccount")
    }

    private fun mostraRegistrazione() {
        FrameRegistrazione(this)
    }

    fun mostraInfoPassword() {
        FrameInfoPassword(this)
    }

    fun apriSelezioneAccessoRegistrazione() {
        val nuovaAttivita = Intent(this, ControllerScelteIniziali::class.java)
        nuovaAttivita.putExtra("tipoAccount", tipoAccountDaRegistrare)
        nuovaAttivita.putExtra("attivitaChiamante", "ControllerRegistrazione")
        startActivity(nuovaAttivita)
    }

    @Throws(EccezioneCampiNonCompilati::class)
    fun verificaCampiCompilati(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty())
            throw EccezioneCampiNonCompilati("Mandatory fields not filled in.")
    }

    @Throws(EccezioneEmailNonValida::class)
    fun verificaEmailFormatoCorretto(email: String) {
        if (!email.contains(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")))
            throw EccezioneEmailNonValida("The email format is not valid.")
    }

    @Throws(EccezioneEmailUsata::class)
    fun verificaEmailUsataAccountStessoTipo(email: String, tipoAccount: String) {
        val registrazioneDB = RegistrazioneDB()

        registrazioneDB.verificaEmailUsataAccountStessoTipo(email, tipoAccount)
    }

    @Throws(EccezionePasswordNonSicura::class)
    fun verificaPasswordSicura(password: String) {
        if (password.length < 8 ||
            !password.contains(Regex("[A-Z]")) ||
            !password.contains(Regex("[0-9]")) ||
            !password.contains(Regex("[!@#\$%^{}&*]"))
        )
            throw EccezionePasswordNonSicura("The password is not secure.")
    }

    fun scegliAssociaCreaProfilo(email: String, password: String) {
        val accountGiaRegistrato = recuperaAccountRegistrato(email)

        // Un account di altro tipo con la stessa email è stato trovato
        if (accountGiaRegistrato != null) {
            associaAccount(accountGiaRegistrato, email, password)
            apriAssociaProfilo()
        }
        // Un account di altro tipo con la stessa email non è stato trovato
        else {
            creaAccount(email, password)
            apriCreaProfilo()
        }
    }

    private fun recuperaAccountRegistrato(email: String): Account? {
        val controlloreScambioDati = RegistrazioneDB()

        return controlloreScambioDati.recuperaAccountTipoDiverso(email, tipoAccountDaRegistrare)
    }

    private fun associaAccount(
        accountGiaRegistrato: Account,
        emailRegistrazione: String,
        passwordRegistrazione: String
    ) {
        // Recupera il profilo dell'account già esistente
        val profilo = accountGiaRegistrato.getProfilo()

        // Crea l'account e inserisci profilo in account
        accountDaRegistrare = when (tipoAccountDaRegistrare) {
            "compratore" -> AccountCompratore(
                emailRegistrazione, passwordRegistrazione, profilo,
                mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf()
            )

            else -> AccountVenditore(
                emailRegistrazione, passwordRegistrazione, profilo,
                mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf()
            )
        }

        // Inserisci account nel profilo
        profilo.addAccount(accountDaRegistrare)
    }

    private fun apriAssociaProfilo() {
        TODO()
    }

    private fun creaAccount(email: String, password: String) {
        // Crea il profilo
        val profilo = Profilo(
            "", "", "", "",
            Date.valueOf(""), "", "", "", "",
            "", "", "", "", mutableListOf()
        )

        // Crea l'account e inserisci profilo in account
        accountDaRegistrare = when (tipoAccountDaRegistrare) {
            "Compratore" -> AccountCompratore(
                email, password, profilo,
                mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf()
            )

            else -> AccountVenditore(
                email, password, profilo,
                mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf()
            )
        }

        // Inserisci account nel profilo
        profilo.addAccount(accountDaRegistrare)

    }

    fun apriCreaProfilo() {
        TODO()
    }

    @Throws(EccezioneCollegamentoSocialNonRiuscito::class)
    fun registrazioneGoogle(): Boolean {
        TODO()
    }

    @Throws(EccezioneCollegamentoSocialNonRiuscito::class)
    fun registrazioneFacebook(): Boolean {
        TODO()
    }

    @Throws(EccezioneCollegamentoSocialNonRiuscito::class)
    fun registrazioneGitHub(): Boolean {
        TODO()
    }

    @Throws(EccezioneCollegamentoSocialNonRiuscito::class)
    fun registrazioneX(): Boolean {
        TODO()
    }

    fun rimuoviMessaggioErrore(layoutDoveEliminareMessaggio: LinearLayout) {
        layoutDoveEliminareMessaggio.removeViewAt(2)
    }

    fun evidenziaCampiErrore(vararg campiDaEvidenziare: TextInputLayout) {
        for (campo in campiDaEvidenziare)
            campo.error = getString(R.string.accesso_erroreCampi)
    }

    fun creaMessaggioErroreEmailGiaUsata(layoutDoveInserireErrore: LinearLayout) {
        val testoMessaggio = getString(R.string.registrazione_erroreEmailGiàUsata)
        val messaggioDiErrore = creaMessaggioErrore(testoMessaggio)

        layoutDoveInserireErrore.addView(messaggioDiErrore, 2)
    }

    fun creaMessaggioErrorePasswordNonSicura(layoutDoveInserireErrore: LinearLayout) {
        val testoMessaggio = getString(R.string.registrazione_errorePasswordNonSicura)
        val messaggioDiErrore = creaMessaggioErrore(testoMessaggio)

        layoutDoveInserireErrore.addView(messaggioDiErrore, 2)
    }

    fun creaMessaggioErroreCampiObbligatoriNonCompilati(layoutDoveInserireErrore: LinearLayout) {
        val testoMessaggio =
            getString(R.string.registrazione_erroreCampiObbligatoriNonCompilati)
        val messaggioDiErrore = creaMessaggioErrore(testoMessaggio)

        layoutDoveInserireErrore.addView(messaggioDiErrore, 2)
    }

    fun creaMessaggioErroreRegistrazioneSocial(layoutDoveInserireErrore: LinearLayout) {
        val testoMessaggio = getString(R.string.registrazione_erroreRegistrazioneSocial)
        val messaggioDiErrore = creaMessaggioErrore(testoMessaggio)

        layoutDoveInserireErrore.addView(messaggioDiErrore, 2)
    }

    fun creaMessaggioErroreFormatoEmail(layoutDoveInserireErrore: LinearLayout) {
        val testoMessaggio = getString(R.string.registrazione_erroreFormatoEmail)
        val messaggioDiErrore = creaMessaggioErrore(testoMessaggio)

        layoutDoveInserireErrore.addView(messaggioDiErrore, 2)
    }

    private fun creaMessaggioErrore(testoMessaggio: String): MaterialTextView {
        val messaggio = MaterialTextView(this)

        messaggio.text = testoMessaggio

        messaggio.textSize = 20f

        val grigio = resources.getColor(R.color.grigio, theme)
        messaggio.setBackgroundColor(grigio)

        val rosso = resources.getColor(R.color.rosso, theme)
        messaggio.setTextColor(rosso)

        val parametriLayoutTextView =
            ViewGroup.LayoutParams(
                420 * resources.displayMetrics.density.toInt(),
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        messaggio.layoutParams = parametriLayoutTextView

        messaggio.setBackgroundResource(R.drawable.errore)

        messaggio.setPadding(60, 0, 0, 0)

        return messaggio
    }

    fun rimuoviErroreCampo(vararg campiEvidenziati: TextInputLayout) {
        for (campo in campiEvidenziati)
            campo.error = null
    }
}
