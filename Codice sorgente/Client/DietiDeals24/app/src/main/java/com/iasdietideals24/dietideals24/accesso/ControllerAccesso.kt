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
import com.iasdietideals24.dietideals24.scelteIniziali.ControllerScelteIniziali


class ControllerAccesso : AppCompatActivity() {
    private var tipoAccount: String = ""

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
        tipoAccount = stringaTipoAccount
        dati?.remove("tipoAccount")
    }

    private fun mostraAccesso() {
        FrameAccesso(this)
    }

    fun getTipoAccount(): String {
        return tipoAccount
    }

    fun accedi(email: String, password: String) {
        TODO("Not yet implemented")
    }

    fun apriSelezioneAccessoRegistrazione() {
        cambiaAttivita(ControllerScelteIniziali::class.java)
    }

    private fun cambiaAttivita(controllerAttivita: Class<*>) {
        val nuovaAttivita = Intent(this, controllerAttivita)
        nuovaAttivita.putExtra("tipoAccount", tipoAccount)
        nuovaAttivita.putExtra("attivitaChiamante", "ControllerAccesso")
        startActivity(nuovaAttivita)
    }

    fun accessoGoogle() {
        TODO("Not yet implemented")
    }

    fun accessoFacebook() {
        TODO("Not yet implemented")
    }

    fun accessoGitHub() {
        TODO("Not yet implemented")
    }

    fun accessoConX() {
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
}