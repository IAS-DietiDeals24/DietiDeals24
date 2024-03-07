package com.iasdietideals24.dietideals24.registrazione

import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R


class FrameRegistrazione(private val controller: ControllerRegistrazione) {
    private lateinit var tipoAccount: MaterialTextView
    private lateinit var campoEmail: TextInputLayout
    private lateinit var email: TextInputEditText
    private lateinit var campoPassword: TextInputLayout
    private lateinit var password: TextInputEditText
    private lateinit var pulsanteIndietro: ImageButton
    private lateinit var pulsanteAvanti: MaterialButton
    private lateinit var pulsanteGoogle: ShapeableImageView
    private lateinit var pulsanteFacebook: ShapeableImageView
    private lateinit var pulsanteGitHub: ShapeableImageView
    private lateinit var pulsanteX: ShapeableImageView
    private lateinit var layout: LinearLayout

    init {
        controller.setContentView(R.layout.registrazione)

        trovaElementiInterfaccia()

        impostaMessaggioCorpo()

        impostaEventiClick()

        impostaEventiDiCambiamentoCampi()
    }

    private fun trovaElementiInterfaccia() {
        tipoAccount = controller.findViewById(R.id.registrazione_tipoAccount)
        campoEmail = controller.findViewById(R.id.registrazione_campoEmail)
        email = controller.findViewById(R.id.registrazione_email)
        campoPassword = controller.findViewById(R.id.registrazione_campoPassword)
        password = controller.findViewById(R.id.registrazione_password)
        pulsanteIndietro = controller.findViewById(R.id.registrazione_pulsanteIndietro)
        pulsanteAvanti = controller.findViewById(R.id.registrazione_pulsanteAvanti)
        pulsanteGoogle = controller.findViewById(R.id.registrazione_pulsanteGoogle)
        pulsanteFacebook = controller.findViewById(R.id.registrazione_pulsanteFacebook)
        pulsanteGitHub = controller.findViewById(R.id.registrazione_pulsanteGitHub)
        pulsanteX = controller.findViewById(R.id.registrazione_pulsanteX)
        layout = controller.findViewById(R.id.registrazione_linearLayout)
    }

    private fun impostaMessaggioCorpo() {
        when (controller.getTipoAccount()) {
            "compratore" -> {
                val stringaTipoAccount = controller.getString(R.string.tipoAccount_compratore)
                tipoAccount.text = controller.getString(
                    R.string.accessoregistrazione_tipoAccount,
                    stringaTipoAccount
                )
            }

            "venditore" -> {
                val stringaTipoAccount = controller.getString(R.string.tipoAccount_venditore)
                tipoAccount.text = controller.getString(
                    R.string.accessoregistrazione_tipoAccount,
                    stringaTipoAccount
                )
            }
        }
    }

    private fun impostaEventiClick() {
        campoPassword.setEndIconOnClickListener { clickInfoPassword() }

        pulsanteIndietro.setOnClickListener { clickIndietro() }

        pulsanteAvanti.setOnClickListener { clickAvanti() }

        pulsanteGoogle.setOnClickListener { clickGoogle() }

        pulsanteFacebook.setOnClickListener { clickFacebook() }

        pulsanteGitHub.setOnClickListener { clickGitHub() }

        pulsanteX.setOnClickListener { clickX() }
    }

    private fun clickInfoPassword() {
        controller.mostraInfoPassword()
    }

    private fun clickIndietro() {
        controller.apriSchermataAccessoRegistrazione()
    }

    private fun clickAvanti() {
        val email = estraiTestoDaElemento(email)
        val password = estraiTestoDaElemento(password)
        controller.registrati(email, password)
    }

    private fun clickGoogle() {
        controller.registrazioneGoogle()
    }

    private fun clickFacebook() {
        controller.registrazioneFacebook()
    }

    private fun clickGitHub() {
        controller.registrazioneGitHub()
    }

    private fun clickX() {
        controller.registrazioneX()
    }

    private fun estraiTestoDaElemento(elemento: TextInputEditText): String {
        val testoElemento = elemento.text
        return testoElemento.toString()
    }

    private fun impostaEventiDiCambiamentoCampi() {
        email.addTextChangedListener {
            controller.rimuoviErroreCampo(campoEmail)
        }

        password.addTextChangedListener {
            controller.rimuoviErroreCampo(campoPassword)
        }
    }

    private fun erroreEmailGiaUsata() {
        controller.creaMessaggioErroreEmailGiaUsata(layout)
        controller.evidenziaCampiErrore(campoEmail)
    }

    private fun errorePasswordNonSicura() {
        controller.creaMessaggioErrorePasswordNonSicura(layout)
        controller.evidenziaCampiErrore(campoPassword)
    }

    private fun erroreCampiObbligatoriNonCompilati() {
        controller.creaMessaggioErroreCampiObbligatoriNonCompilati(layout)
        controller.evidenziaCampiErrore(campoEmail, campoPassword)
    }

    private fun erroreRegistrazioneSocial() {
        controller.creaMessaggioErroreRegistrazioneSocial(layout)
    }

}
