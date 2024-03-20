package com.iasdietideals24.dietideals24.registrazione

import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.eccezioni.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.eccezioni.EccezioneCollegamentoSocialNonRiuscito
import com.iasdietideals24.dietideals24.eccezioni.EccezioneEmailNonValida
import com.iasdietideals24.dietideals24.eccezioni.EccezioneEmailUsata
import com.iasdietideals24.dietideals24.eccezioni.EccezionePasswordNonSicura


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
    private lateinit var linearLayout: LinearLayout
    private lateinit var constraintLayout: ConstraintLayout

    init {
        controller.setContentView(R.layout.registrazione)

        trovaElementiInterfaccia()

        // Al fine di rendere il popup di info password più visibile, il frame di registrazione ha
        // una cortina nera come foreground.
        // Viene resa invisibile quando la pagina di registrazione è creata e quando è aperto il
        // popup.
        // Viene resa invisibile quando il popup è chiuso.
        constraintLayout.foreground.alpha = 0

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
        linearLayout = controller.findViewById(R.id.registrazione_linearLayout)
        constraintLayout = controller.findViewById(R.id.registrazione_constraintLayout)
    }

    private fun impostaMessaggioCorpo() {
        when (controller.tipoAccountDaRegistrare) {
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
        controller.apriSelezioneAccessoRegistrazione()
    }

    private fun clickAvanti() {
        val email = estraiTestoDaElemento(email)
        val password = estraiTestoDaElemento(password)

        effettuaVerificheEAvanza(email, password)
    }

    private fun effettuaVerificheEAvanza(email: String, password: String) {
        try {
            controller.verificaCampiCompilati(email, password)

            controller.verificaEmailFormatoCorretto(email)

            val tipoAccount = controller.tipoAccountDaRegistrare
            controller.verificaEmailUsataAccountStessoTipo(email, tipoAccount)

            controller.verificaPasswordSicura(password)

            controller.scegliAssociaCreaProfilo(email, password)
        } catch (eccezione: EccezioneCampiNonCompilati) {
            controller.rimuoviMessaggioErrore(linearLayout)
            erroreCampiObbligatoriNonCompilati()
        } catch (eccezione: EccezioneEmailNonValida) {
            controller.rimuoviMessaggioErrore(linearLayout)
            erroreFormatoEmail()
        } catch (eccezione: EccezioneEmailUsata) {
            controller.rimuoviMessaggioErrore(linearLayout)
            erroreEmailGiaUsata()
        } catch (eccezione: EccezionePasswordNonSicura) {
            controller.rimuoviMessaggioErrore(linearLayout)
            errorePasswordNonSicura()
        }
    }

    private fun clickGoogle() {
        try {
            controller.registrazioneGoogle()

            controller.apriCreaProfilo()
        } catch (eccezione: EccezioneCollegamentoSocialNonRiuscito) {
            controller.rimuoviMessaggioErrore(linearLayout)
            erroreRegistrazioneSocial()
        }
    }

    private fun clickFacebook() {
        try {
            controller.registrazioneFacebook()

            controller.apriCreaProfilo()
        } catch (eccezione: EccezioneCollegamentoSocialNonRiuscito) {
            controller.rimuoviMessaggioErrore(linearLayout)
            erroreRegistrazioneSocial()
        }
    }

    private fun clickGitHub() {
        try {
            controller.registrazioneGitHub()

            controller.apriCreaProfilo()
        } catch (eccezione: EccezioneCollegamentoSocialNonRiuscito) {
            controller.rimuoviMessaggioErrore(linearLayout)
            erroreRegistrazioneSocial()
        }
    }

    private fun clickX() {
        try {
            controller.registrazioneX()

            controller.apriCreaProfilo()
        } catch (eccezione: EccezioneCollegamentoSocialNonRiuscito) {
            controller.rimuoviMessaggioErrore(linearLayout)
            erroreRegistrazioneSocial()
        }
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
        controller.creaMessaggioErroreEmailGiaUsata(linearLayout)
        controller.evidenziaCampiErrore(campoEmail)
    }

    private fun errorePasswordNonSicura() {
        controller.creaMessaggioErrorePasswordNonSicura(linearLayout)
        controller.evidenziaCampiErrore(campoPassword)
    }

    private fun erroreCampiObbligatoriNonCompilati() {
        controller.creaMessaggioErroreCampiObbligatoriNonCompilati(linearLayout)
        controller.evidenziaCampiErrore(campoEmail, campoPassword)
    }

    private fun erroreRegistrazioneSocial() {
        controller.creaMessaggioErroreRegistrazioneSocial(linearLayout)
    }

    private fun erroreFormatoEmail() {
        controller.creaMessaggioErroreFormatoEmail(linearLayout)
        controller.evidenziaCampiErrore(campoEmail)
    }

}
