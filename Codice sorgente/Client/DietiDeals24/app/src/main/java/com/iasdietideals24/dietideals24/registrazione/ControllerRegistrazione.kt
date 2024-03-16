package com.iasdietideals24.dietideals24.registrazione

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.model.Profilo
import com.iasdietideals24.dietideals24.model.account.Account
import com.iasdietideals24.dietideals24.model.account.AccountCompratore
import com.iasdietideals24.dietideals24.model.account.AccountVenditore
import com.iasdietideals24.dietideals24.scelteIniziali.ControllerScelteIniziali
import java.sql.Date


class ControllerRegistrazione : AppCompatActivity() {
    private var _tipoAccountDaRegistrare: String = ""
    private var _accountDaRegistrare: Account? = null

    var tipoAccountDaRegistrare: String
        get() = _tipoAccountDaRegistrare
        set(valore) {
            _tipoAccountDaRegistrare = valore
        }

    private var accountDaRegistrare: Account?
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

    fun isCampiCompilati(email: String, password: String): Boolean {
        return verificaCampiCompilati(email, password)
    }

    private fun verificaCampiCompilati(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    fun isEmailFormatoCorretto(email: String): Boolean {
        return verificaEmailFormatoCorretto(email)
    }

    private fun verificaEmailFormatoCorretto(email: String): Boolean {
        return email.contains(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))
    }

    fun isEmailUsataAccountStessoTipo(email: String, tipoAccount: String): Boolean {
        return verificaEmailUsataAccountStessoTipo(email, tipoAccount)
    }

    private fun verificaEmailUsataAccountStessoTipo(email: String, tipoAccount: String): Boolean {
        val registrazioneDB = RegistrazioneDB()

        return registrazioneDB.verificaEmailUsataAccountStessoTipo(email, tipoAccount)
    }

    fun isPasswordSicura(password: String): Boolean {
        return verificaPasswordSicura(password)
    }

    private fun verificaPasswordSicura(password: String): Boolean {
        return password.length >= 8 &&
                password.contains(Regex("[A-Z]")) &&
                password.contains(Regex("[0-9]")) &&
                password.contains(Regex("[!@#\$%^{}&*]"))
    }

    fun scegliAssociaCreaProfilo(email: String, password: String) {
        recuperaAccountRegistrato(email)

        if (accountDaRegistrare != null)
            apriAssociaProfilo()
        else {
            creaAccount(email, password)
            apriCreaProfilo()
        }
    }

    private fun recuperaAccountRegistrato(email: String) {
        val controlloreScambioDati = RegistrazioneDB()

        accountDaRegistrare =
            controlloreScambioDati.recuperaAccountTipoDiverso(email, tipoAccountDaRegistrare)
    }

    private fun creaAccount(email: String, password: String) {
        val profilo = Profilo(
            "", "", "", "",
            Date.valueOf(""), "", "", "", "",
            "", "", "", "", mutableListOf()
        )

        when (tipoAccountDaRegistrare) {
            "Compratore" -> accountDaRegistrare = AccountCompratore(
                email, password, profilo,
                mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf()
            )

            "Venditore" -> accountDaRegistrare = AccountVenditore(
                email, password, profilo,
                mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf()
            )
        }
    }

    private fun apriAssociaProfilo() {
        //TODO
    }

    fun mostraInfoPassword() {
        FrameInfoPassword(this)
    }

    fun apriSelezioneAccessoRegistrazione() {
        cambiaAttivita(ControllerScelteIniziali::class.java)
    }

    private fun cambiaAttivita(controllerAttivita: Class<*>) {
        val nuovaAttivita = Intent(this, controllerAttivita)
        nuovaAttivita.putExtra("tipoAccount", tipoAccountDaRegistrare)
        nuovaAttivita.putExtra("attivitaChiamante", "ControllerRegistrazione")
        startActivity(nuovaAttivita)
    }

    fun apriCreaProfilo() {
        //TODO
    }

    fun registrazioneGoogle(): Boolean {
        //TODO

        return false
    }

    fun registrazioneFacebook(): Boolean {
        //TODO

        return false
    }

    fun registrazioneGitHub(): Boolean {
        //TODO

        return false
    }

    fun registrazioneX(): Boolean {
        //TODO

        return false
    }

    fun evidenziaCampiErrore(vararg campiDaEvidenziare: TextInputLayout) {
        for (campo in campiDaEvidenziare)
            campo.error = getString(R.string.accesso_erroreCampi)
    }

    fun rimuoviErroreCampo(vararg campiEvidenziati: TextInputLayout) {
        for (campo in campiEvidenziati)
            campo.error = null
    }

    fun rimuoviMessaggioErrore(layoutDoveEliminareMessaggio: LinearLayout) {
        layoutDoveEliminareMessaggio.removeViewAt(2)
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
}
