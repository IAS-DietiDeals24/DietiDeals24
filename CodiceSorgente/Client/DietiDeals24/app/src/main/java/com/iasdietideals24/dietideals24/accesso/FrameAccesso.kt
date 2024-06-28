package com.iasdietideals24.dietideals24.accesso

import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.eccezioni.EccezioneAccountNonEsistente
import com.iasdietideals24.dietideals24.eccezioni.EccezioneCollegamentoSocialNonRiuscito


class FrameAccesso(private val controller: ControllerAccesso) {
    private lateinit var tipoAccount: MaterialTextView
    private lateinit var campoEmail: TextInputLayout
    private lateinit var email: TextInputEditText
    private lateinit var campoPassword: TextInputLayout
    private lateinit var password: TextInputEditText
    private lateinit var pulsanteIndietro: ImageButton
    private lateinit var pulsanteAccedi: MaterialButton
    private lateinit var pulsanteGoogle: ShapeableImageView
    private lateinit var pulsanteFacebook: ShapeableImageView
    private lateinit var pulsanteGitHub: ShapeableImageView
    private lateinit var pulsanteX: ShapeableImageView
    private lateinit var layout: LinearLayout

    init {
        controller.setContentView(R.layout.accesso)

        trovaElementiInterfaccia()

        impostaMessaggioCorpo()

        impostaEventiClick()

        impostaEventiDiCambiamentoCampi()
    }

    private fun trovaElementiInterfaccia() {
        tipoAccount = controller.findViewById(R.id.accesso_tipoAccount)
        campoEmail = controller.findViewById(R.id.accesso_campoEmail)
        email = controller.findViewById(R.id.accesso_email)
        campoPassword = controller.findViewById(R.id.accesso_campoPassword)
        password = controller.findViewById(R.id.accesso_password)
        pulsanteIndietro = controller.findViewById(R.id.accesso_pulsanteIndietro)
        pulsanteAccedi = controller.findViewById(R.id.accesso_pulsanteAccedi)
        pulsanteGoogle = controller.findViewById(R.id.accesso_pulsanteGoogle)
        pulsanteFacebook = controller.findViewById(R.id.accesso_pulsanteFacebook)
        pulsanteGitHub = controller.findViewById(R.id.accesso_pulsanteGitHub)
        pulsanteX = controller.findViewById(R.id.accesso_pulsanteX)
        layout = controller.findViewById(R.id.accesso_linearLayout)
    }

    private fun impostaMessaggioCorpo() {
        when (controller.tipoAccountDaLoggare) {
            "compratore" -> {
                val stringaTipoAccount = controller.getString(R.string.tipoAccount_compratore)
                tipoAccount.text = controller.getString(
                    R.string.selezioneAccessoRegistrazione_tipoAccount,
                    stringaTipoAccount
                )
            }

            "venditore" -> {
                val stringaTipoAccount = controller.getString(R.string.tipoAccount_venditore)
                tipoAccount.text = controller.getString(
                    R.string.selezioneAccessoRegistrazione_tipoAccount,
                    stringaTipoAccount
                )
            }
        }
    }

    private fun impostaEventiClick() {
        pulsanteIndietro.setOnClickListener { clickIndietro() }

        pulsanteAccedi.setOnClickListener { clickAccedi() }

        pulsanteGoogle.setOnClickListener { clickGoogle() }

        pulsanteFacebook.setOnClickListener { clickFacebook() }

        pulsanteGitHub.setOnClickListener { clickGitHub() }

        pulsanteX.setOnClickListener { clickX() }
    }

    private fun clickIndietro() {
        controller.apriSelezioneAccessoRegistrazione()
    }

    private fun clickAccedi() {
        val email = estraiTestoDaElemento(email)
        val password = estraiTestoDaElemento(password)

        try {
            controller.accedi(email, password)
        } catch (eccezione: EccezioneAccountNonEsistente) {
            controller.rimuoviMessaggioErrore(layout)
            erroreCredenzialiNonCorrette()
        }
    }

    private fun estraiTestoDaElemento(elemento: TextInputEditText): String {
        val testoElemento = elemento.text
        return testoElemento.toString()
    }

    private fun clickGoogle() {
        try {
            controller.accessoGoogle()
        } catch (eccezione: EccezioneCollegamentoSocialNonRiuscito) {
            controller.rimuoviMessaggioErrore(layout)
            erroreAccessoSocial()
        }
    }

    private fun clickFacebook() {
        try {
            controller.accessoFacebook()
        } catch (eccezione: EccezioneCollegamentoSocialNonRiuscito) {
            controller.rimuoviMessaggioErrore(layout)
            erroreAccessoSocial()
        }
    }

    private fun clickGitHub() {
        try {
            controller.accessoGitHub()
        } catch (eccezione: EccezioneCollegamentoSocialNonRiuscito) {
            controller.rimuoviMessaggioErrore(layout)
            erroreAccessoSocial()
        }
    }

    private fun clickX() {
        try {
            controller.accessoConX()
        } catch (eccezione: EccezioneCollegamentoSocialNonRiuscito) {
            controller.rimuoviMessaggioErrore(layout)
            erroreAccessoSocial()
        }
    }

    private fun erroreCredenzialiNonCorrette() {
        controller.creaMessaggioErroreCredenzialiNonCorrette(layout)
        controller.evidenziaCampiErrore(campoEmail, campoPassword)
    }

    private fun erroreAccessoSocial() {
        controller.creaMessaggioErroreSocial(layout)
    }

    private fun impostaEventiDiCambiamentoCampi() {
        email.addTextChangedListener {
            controller.rimuoviErroreCampo(campoEmail)
        }

        password.addTextChangedListener {
            controller.rimuoviErroreCampo(campoPassword)
        }
    }
}
