package com.iasdietideals24.dietideals24.accesso

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.eccezioni.EccezioneAccountNonEsistente
import com.iasdietideals24.dietideals24.eccezioni.EccezioneCollegamentoSocialNonRiuscito
import com.iasdietideals24.dietideals24.model.account.Account
import com.iasdietideals24.dietideals24.scelteIniziali.ControllerScelteIniziali


class ControllerAccesso : AppCompatActivity() {
    private lateinit var _tipoAccountDaLoggare: String
    private lateinit var _accountDaLoggare: Account

    var tipoAccountDaLoggare: String
        get() = _tipoAccountDaLoggare
        set(valore) {
            _tipoAccountDaLoggare = valore
        }

    private var accountDaLoggare: Account
        get() = _accountDaLoggare
        set(valore) {
            _accountDaLoggare = valore
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recuperaAttributiPassati()

        mostraAccesso()
    }

    private fun recuperaAttributiPassati() {
        val vecchiaAttivita = intent
        val dati = vecchiaAttivita.extras
        val datoTipoAccount = dati?.getString("tipoAccount")
        val stringaTipoAccount = datoTipoAccount.toString()
        tipoAccountDaLoggare = stringaTipoAccount
        dati?.remove("tipoAccount")
    }

    private fun mostraAccesso() {
        FrameAccesso(this)
    }

    fun apriSelezioneAccessoRegistrazione() {
        val nuovaAttivita = Intent(this, ControllerScelteIniziali::class.java)
        nuovaAttivita.putExtra("tipoAccount", tipoAccountDaLoggare)
        nuovaAttivita.putExtra("attivitaChiamante", "ControllerAccesso")
        startActivity(nuovaAttivita)
    }

    @Throws(EccezioneAccountNonEsistente::class)
    fun accedi(email: String, password: String) {
        recuperaAccount(email, password)

        apriHome()
    }

    @Throws(EccezioneAccountNonEsistente::class)
    private fun recuperaAccount(email: String, password: String) {
        val controlloreScambioDati = AccessoDB()

        accountDaLoggare = controlloreScambioDati.recuperaAccount(email, password)
    }

    private fun apriHome() {
        TODO()
    }

    @Throws(EccezioneCollegamentoSocialNonRiuscito::class)
    fun accessoGoogle() {
        TODO()
    }

    @Throws(EccezioneCollegamentoSocialNonRiuscito::class)
    fun accessoFacebook() {
        TODO()
    }

    @Throws(EccezioneCollegamentoSocialNonRiuscito::class)
    fun accessoGitHub() {
        TODO()
    }

    @Throws(EccezioneCollegamentoSocialNonRiuscito::class)
    fun accessoConX() {
        TODO()
    }

    fun rimuoviMessaggioErrore(layoutDoveEliminareMessaggio: LinearLayout) {
        layoutDoveEliminareMessaggio.removeViewAt(2)
    }

    fun evidenziaCampiErrore(vararg campiDaEvidenziare: TextInputLayout) {
        for (campo in campiDaEvidenziare)
            campo.error = getString(R.string.accesso_erroreCampi)
    }

    fun creaMessaggioErroreCredenzialiNonCorrette(layoutDoveInserireErrore: LinearLayout) {
        val testoMessaggio = getString(R.string.accesso_erroreCredenzialiNonCorrette)
        val messaggioDiErrore = creaMessaggioErrore(testoMessaggio)

        layoutDoveInserireErrore.addView(messaggioDiErrore, 2)
    }

    fun creaMessaggioErroreSocial(layoutDoveInserireErrore: LinearLayout) {
        val testoMessaggio = getString(R.string.accesso_erroreSocial)
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
            LayoutParams(420 * resources.displayMetrics.density.toInt(), MATCH_PARENT)
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