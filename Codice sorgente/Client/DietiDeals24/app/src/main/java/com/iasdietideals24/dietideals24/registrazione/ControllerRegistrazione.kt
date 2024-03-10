package com.iasdietideals24.dietideals24.registrazione

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.scelteIniziali.ControllerScelteIniziali


class ControllerRegistrazione : AppCompatActivity() {
    private var tipoAccount: String = ""

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
        tipoAccount = stringaTipoAccount
        dati?.remove("tipoAccount")
    }

    private fun mostraRegistrazione() {
        FrameRegistrazione(this)
    }

    fun getTipoAccount(): String {
        return tipoAccount
    }

    fun registrati(email: String, password: String) {
        TODO("Not yet implemented")
    }

    fun mostraInfoPassword() {
        FrameInfoPassword(this)
    }

    fun apriSelezioneAccessoRegistrazione() {
        cambiaAttivita(ControllerScelteIniziali::class.java)
    }

    private fun cambiaAttivita(controllerAttivita: Class<*>) {
        val nuovaAttivita = Intent(this, controllerAttivita)
        nuovaAttivita.putExtra("tipoAccount", tipoAccount)
        nuovaAttivita.putExtra("attivitaChiamante", "ControllerRegistrazione")
        startActivity(nuovaAttivita)
    }

    fun registrazioneGoogle() {
        TODO("Not yet implemented")
    }

    fun registrazioneFacebook() {
        TODO("Not yet implemented")
    }

    fun registrazioneGitHub() {
        TODO("Not yet implemented")
    }

    fun registrazioneX() {
        TODO("Not yet implemented")
    }

    fun evidenziaCampiErrore(vararg campiDaEvidenziare: TextInputLayout) {
        for (campo in campiDaEvidenziare)
            campo.error = getString(R.string.accesso_erroreCampi)
    }

    fun rimuoviErroreCampo(vararg campiEvidenziati: TextInputLayout) {
        for (campo in campiEvidenziati)
            campo.error = null
    }

    fun creaMessaggioErroreEmailGiaUsata(layoutDoveInserireErrore: LinearLayout) {
        val testoMessaggio = getString(R.string.registrazione_erroreEmailGiaUsata)
        val messaggioDiErrore = creaMessaggioErrore(testoMessaggio)

        layoutDoveInserireErrore.addView(messaggioDiErrore)
    }

    fun creaMessaggioErrorePasswordNonSicura(layoutDoveInserireErrore: LinearLayout) {
        val testoMessaggio = getString(R.string.registrazione_errorePasswordNonSicura)
        val messaggioDiErrore = creaMessaggioErrore(testoMessaggio)

        layoutDoveInserireErrore.addView(messaggioDiErrore)
    }

    fun creaMessaggioErroreCampiObbligatoriNonCompilati(layoutDoveInserireErrore: LinearLayout) {
        val testoMessaggio = getString(R.string.registrazione_erroreCampiObbligatoriNonCompilati)
        val messaggioDiErrore = creaMessaggioErrore(testoMessaggio)

        layoutDoveInserireErrore.addView(messaggioDiErrore)
    }

    fun creaMessaggioErroreRegistrazioneSocial(layoutDoveInserireErrore: LinearLayout) {
        val testoMessaggio = getString(R.string.registrazione_erroreRegistrazioneSocial)
        val messaggioDiErrore = creaMessaggioErrore(testoMessaggio)

        layoutDoveInserireErrore.addView(messaggioDiErrore)
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
